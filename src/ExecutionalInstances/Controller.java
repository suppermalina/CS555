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
	private double lambda = 0.10;
	private long endingTime = 5000;
	public static Deque<Task> localLogRecorder;
	public static boolean flag = false;
	public static Set<Timer> timerPool;
	private Lock timerLock;

	// These two lists were in the Simulator class
	public static StateList log;
	public static TaskList tasks;
	public static StatisticalCounter counter;
	private ReportGenerator reporter;

	public static Timer monitor;

	private Controller() {
		System.out.println("Controller ready");
	}

	private void setUp() {
		counter = (StatisticalCounter) Generator.getContainer("counter");
		log = (StateList) Generator.getContainer("statelist");
		tasks = (TaskList) Generator.getContainer("tasklist");
		reporter = new ReportGenerator();
		monitor = new Timer();
		timerPool = new HashSet<Timer>();
		timerLock = new ReentrantLock();
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
		System.out.println("signals() was started at: " + initialTime);
		while (StatisticalClock.CLOCK() <= endingTime) {
			if (StatisticalClock.CLOCK() % 5l == 0) {

				reporter.writeLog("--------------------------------------------------------------------");
				reporter.writeLog("There are total " + simulator.currentState() + " customers in the system at: "
						+ StatisticalClock.CLOCK());
				reporter.writeLog("--------------------------------------------------------------------");
			}
			if (initialTime <= 260 || flag == true) {
				// predictTime here is used for generating the coming new
				// customer
				long predictTime = (long) (RandomNumberGenerator.getInstance(lambda) * 1000);
				// The task should be sent to the tasklist
				GenerateCustomer tempGenerateSignal = (GenerateCustomer) Generator.getTask("generating");
				tempGenerateSignal.setInterval(predictTime);
				Controller.tasks.takeTaskIn(tempGenerateSignal);

				if (timerLock.tryLock()) {
					try {
						monitor.schedule(tempGenerateSignal, predictTime);
					} finally {
						timerLock.unlock();
					}
				}
				initialTime = -1;
				flag = false;
			}
		}
		monitor.cancel();

		return true;
	}

	public void start() {
		setUp();
		generateLogWriter();
		simulator.setUp();
		System.out.println("ok");
		boolean starts = simulator.specialSituation_SystemFull();
		simulator.modify();
		writeLog("Simulator starts at: " + StatisticalClock.CLOCK() + ", ending time is: " + this.endingTime);
		if (starts) {
			signals();
			this.exportLog();
			ReportGenerator.generatingReport();
			ReportGenerator.closeLog();
		}
	}
}
