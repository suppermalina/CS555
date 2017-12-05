package ExecutionalInstances;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import ContainersInstance.Server;
import ContainersInstance.StateList;
import ContainersInstance.TaskList;
import EventsInstances.GenerateCustomer;
import EventsInstances.PopCustomerOut;
import Model.*;
import TestQuatz.TestJob;

public class Controller {
	// These two variables used be a part of GenerateCustomer and PopCustomerOut
	// accordingly
	// but I think these two variables should be set and controlled by the
	// controller
	// The average arriving rate
	// The service rate
	private double miu = 1;
	private long endingTime = 5000;
	public static Deque<Task> localLogRecorder;
	private static Scheduler schedule = null;
	private static JobBuilder builder = null;
	public static boolean flag = false;
	private ReportGenerator reporter;
	// These two lists were in the Simulator class
	public static StateList log;
	public static TaskList tasks;

	private static JobDataMap data = null;

	private Controller() {
		System.out.println("Controller ready");
		reporter = new ReportGenerator();
		org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");
	}

	private void setUp() {

		log = (StateList) Generator.getContainer("statelist");
		tasks = (TaskList) Generator.getContainer("tasklist");
		try {
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			schedule = schedFact.getScheduler();
			schedule.start();
			builder = JobBuilder.newJob(GenerateCustomer.class);
			data = new JobDataMap();
		} catch (SchedulerException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Controller instance = null;;

	public static synchronized Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	private static Simulator simulator = Simulator.getInstance();
	
	// private Map<Integer, Task> timerPool = new HashMap<Integer, Task>();

	public synchronized void addTask(Task t) {
		tasks.takeTaskIn(t);
		// timerPool.put(t.getId(), t);
	}

	private static FileWriter output;
	private static BufferedWriter writer;

	private void generateLogWriter() {
		try {
			output = new FileWriter(new File("/Users/mali/Desktop/555Project/log.txt"));
			writer = new BufferedWriter(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeLog(String str) {
		try {
			writer.write(str);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void exportLog() {
		try {
			if (writer != null) {
				writer.flush();
				output.close();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean signals() {
		long initialTime = StatisticalClock.CLOCK();
		writeLog("signals() was started at: " + initialTime);
		System.out.println("Is it here?");
		int counter = 1;
		while (StatisticalClock.CLOCK() <= endingTime) {
			System.out.println("the " + counter++ + "th loop");
			if (initialTime <= 5 || flag == true) {
				long predictiTime = (long) (RandomNumberGenerator.getInstance(miu) * 1000);
				// The task should be sent to the tasklist
				GenerateCustomer tempGenerateSignal = new GenerateCustomer();
				tempGenerateSignal.setInterval(predictiTime);
				tempGenerateSignal.markCustomerID(tempGenerateSignal.getId());
				tempGenerateSignal.setInterval(predictiTime);
				Controller.tasks.takeTaskIn(tempGenerateSignal);
				JobDetail job = builder.newJob(GenerateCustomer.class).withIdentity(tempGenerateSignal.toString()).storeDurably(false)
						.usingJobData(data).build();
				Trigger trigger = TriggerBuilder.newTrigger()
						.withIdentity("Trigger for " + tempGenerateSignal.toString()).startAt(new Date(predictiTime))
						.build();
				try {
					schedule.scheduleJob(job, trigger);
				} catch (SchedulerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				initialTime = -1;
				flag = false;
				if (StatisticalClock.CLOCK() % 5l == 0) {
					
					reporter.writeLog("--------------------------------------------------------------------");
					reporter.writeLog("There are total " + simulator.currentState() + " customers in the system at: "
							+ StatisticalClock.CLOCK());
					reporter.writeLog("--------------------------------------------------------------------");
				}
			}
		}
		try {
			while (Server.monitor.getJobGroupNames().isEmpty() && schedule.getJobGroupNames().isEmpty()) {
				exportLog();
				reporter.exportLog();
				System.out.println("closed");
				System.exit(1);
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	// private int counter = 1;

	// This method is used to generate signals for generating new customer
	/*
	 * private boolean signals() { long initialTime = StatisticalClock.CLOCK();
	 * // Test.averageSignalInitiatingTime += initialTime;
	 * writeLog("signals() was started at: " + initialTime); while
	 * (StatisticalClock.CLOCK() <= endingTime) {
	 * //this.writeLog("This is the counter " + counter++ +
	 * "th loop in the signals loop"); // A generating signal should be
	 * generated once at the very // beginning // or a new customer is generated
	 * //this.writeLog("Now the flag is " + flag + " before doit()"); flag =
	 * doit(); //this.writeLog("Now the flag is " + flag + " after doit()"); //
	 * this.writeLog("Now the flag is " + flag + " after receiver()"); if
	 * (initialTime == 0 || flag == true) { delay = (long)
	 * (RandomNumberGenerator.getInstance(lambda) * 1000); GenerateCustomer
	 * tempCust = (GenerateCustomer) Generator.getTask("generating"); //
	 * System.out.println("Is this gener " + tempCust.getId() + " + // " +
	 * tempCust.getTerminalTime()); tasks.takeTaskIn(tempCust); String
	 * information = tempCust.toString() + " being generated at :"; //
	 * System.out.println(tasks.getSize()); // System.out.println(information +
	 * StatisticalClock.CLOCK()); this.writeLog(information +
	 * StatisticalClock.CLOCK()); initialTime = -1; flag = false; }
	 * System.out.println("generating signal skips at: " +
	 * StatisticalClock.CLOCK()); } while (StatisticalClock.CLOCK() >
	 * this.endingTime) { System.out.println(log); System.out.println("Over");
	 * System.exit(1); return true; } return false; }
	 */

	/*
	 * public static void notified(Task t) { if (lock.tryLock()) {
	 * writeLog(t.toString() +
	 * " is holding the ReentrantLock in notified() at: " +
	 * StatisticalClock.CLOCK()); try { if (t == null) {
	 * System.out.println("it's null"); } else {
	 * System.out.println("center received the task"); }
	 * 
	 * // Once a signal task informs the controller that it reaches the // due
	 * // time, it has to be popped out from the tasklist
	 * System.out.println("Task " + t.getTyp() + " update at: " +
	 * StatisticalClock.CLOCK()); //this part used to be update if (t instanceof
	 * GenerateCustomer) { writeLog("Generating signal from " + t.toString() +
	 * " at: " + StatisticalClock.CLOCK()); simulator.generateNewCustomer(t); }
	 * else { writeLog("Poping signal from " + t.toString() + " at: " +
	 * StatisticalClock.CLOCK()); simulator.popCustomer((PopCustomerOut) t); }
	 * SystemState currentState = new SystemState(StatisticalClock.CLOCK(),
	 * simulator.currentState()); log.takeStateIn(currentState); } finally {
	 * writeLog(t.toString() + " is going to unlock the ReentrantLock at: " +
	 * StatisticalClock.CLOCK()); lock.unlock(); } } else {
	 * writeLog(t.toString() +
	 * " is waiting for the ReentrantLock in notified at: " +
	 * StatisticalClock.CLOCK()); } }
	 */

	public boolean start() {
		setUp();
		simulator.setUp();
		generateLogWriter();
		writeLog("Simulator starts at: " + StatisticalClock.CLOCK() + ", ending time is: " + this.endingTime);
		return signals();
	}
}
