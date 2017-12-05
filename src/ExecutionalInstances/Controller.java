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
import ContainersInstance.StatisticalCounter;
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
	private double lambda = 0.50;
	private long endingTime = 10000;
	public static Deque<Task> localLogRecorder;
	private static Scheduler schedule = null;
	private static JobBuilder builder = null;
	public static boolean flag = false;

	// These two lists were in the Simulator class
	public static StateList log;
	public static TaskList tasks;
	public static StatisticalCounter counter;
	private ReportGenerator reporter;
	private static JobDataMap data = null;
	private boolean cheating = true;

	private Controller() {
		System.out.println("Controller ready");
		org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");
	}

	private void setUp() {
		counter = (StatisticalCounter) Generator.getContainer("counter");
		log = (StateList) Generator.getContainer("statelist");
		tasks = (TaskList) Generator.getContainer("tasklist");
		reporter = new ReportGenerator();
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

	private static boolean signal = true;

	public static boolean signalFromController(boolean stop) {
		signal = stop;
		return stop;
	}

	private boolean noMoreCheating() {
		return signalFromController(signal);
	}

	private boolean signals() {
		long initialTime = StatisticalClock.CLOCK();
		writeLog("signals() was started at: " + initialTime);
		System.out.println("signals() was started at: " + initialTime);
		while (StatisticalClock.CLOCK() <= endingTime) {
			if (StatisticalClock.CLOCK() % 5l == 0) {

				reporter.writeLog("--------------------------------------------------------------------");
				reporter.writeLog("There are total " + simulator.currentState() + " customers in the system at: "
						+ StatisticalClock.CLOCK());
				reporter.writeLog("--------------------------------------------------------------------");
			}
			simulator.modify();
			long makeUpForCheating = 0;
			if (cheating) {
				makeUpForCheating = 250;
			} else {
				makeUpForCheating = 0;
			}
			if (initialTime <= 500 || flag == true) {
				// predictTime here is used for generating the coming new
				// customer
				long predictTime = (long) (RandomNumberGenerator.getInstance(lambda) * 1000);
				// The task should be sent to the tasklist
				GenerateCustomer tempGenerateSignal = new GenerateCustomer();
				tempGenerateSignal.setInterval(predictTime);
				Controller.tasks.takeTaskIn(tempGenerateSignal);
				JobDetail job = builder.newJob(GenerateCustomer.class).withIdentity(tempGenerateSignal.toString())
						.usingJobData(data).build();
				Trigger trigger = TriggerBuilder.newTrigger()
						.withIdentity("Trigger for " + tempGenerateSignal.toString()).startAt(new Date(predictTime + makeUpForCheating))
						.build();
				try {
					if (cheating) {
						Controller.writeLog("******************************Earily bird is waiting in the queue**********************************");
					}
					schedule.scheduleJob(job, trigger);
				} catch (SchedulerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				initialTime = -1;
				flag = false;
			}
		}
		try {
			while (Server.monitor.getJobGroupNames().isEmpty() && schedule.getJobGroupNames().isEmpty()) {
				exportLog();
				ReportGenerator.exportLog();
				System.out.println("closed");
				System.exit(1);
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public void start() {
		setUp();
		generateLogWriter();
		simulator.setUp();
		boolean starts = simulator.specialSituation_PartialFull(4);
		
		writeLog("Simulator starts at: " + StatisticalClock.CLOCK() + ", ending time is: " + this.endingTime);
		if (starts) {
			signals();
		}
	}
}
