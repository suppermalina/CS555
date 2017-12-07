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

public class Controller {
	// These two variables used be a part of GenerateCustomer and PopCustomerOut
	// accordingly
	// but I think these two variables should be set and controlled by the
	// controller
	// The average arriving rate
	// The service rate
	private double lambda = 0.5;
	private long endingTime = 10000;
	public static Deque<Task> localLogRecorder;
	public static boolean flag = true;
	public static Set<Timer> timerPool;
	private Lock timerLock;

	// These two lists were in the Simulator class
	public static StateList log;
	public static TaskList tasks;
	public static StatisticalCounter counter;
	public static ReportGenerator reporter;
	public static long initialTime;
	protected static long samplePoint = 50000000;
	private static long recordPoint = 100;
	protected Deque<Long> timeRecorder;
	public static StatisticalClock clock;
	public static Timer monitor;
	private Lock lock;
	public static Simulator simulator;

	Controller() {
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
		timeRecorder = new LinkedList<Long>();
		lock = new ReentrantLock();
		simulator = new Simulator();
	}

	private static Controller instance = null;;

	public static synchronized Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	// private Map<Integer, Task> timerPool = new HashMap<Integer, Task>();

	public synchronized void addTask(Task t) {
		tasks.takeTaskIn(t);
		// timerPool.put(t.getId(), t);
	}

	/*
	 * int testCount; protected void dataForPloting(long inputTime) {
	 * reporter.writeRepor("This is the " + ++testCount +
	 * "th time executing dataForPloting at: " + inputTime / 1000000.0);
	 * 
	 * 
	 * }
	 */

	private boolean signals() {
		System.out.println("signals() was started at: " + initialTime);
		int counter = 0;
		while (clock.CLOCK() <= endingTime) {
			long time = clock.NANOCLOCK();
			long nanoPoint = TimeUnit.NANOSECONDS.toNanos(5000);
			if (time % nanoPoint == 0) {

				//reporter.writeRepor("--------------------------------------------------------------------");
				//reporter.writeRepor("There are total " + simulator.currentState() + " customers in the system at: "
					//	+ clock.CLOCK());
				//reporter.writeRepor("--------------------------------------------------------------------");

				simulator.plotState(time);
				// simulator.dataForPloting(time);
				// time = -1;

			}

			if (initialTime == 0 || flag == true) {
				// predictTime here is used for generating the coming new
				// customer
				long predictTime = (long) (RandomNumberGenerator.getInstance(lambda) * 1000);
				// The task should be sent to the tasklist
				GenerateCustomer tempGenerateSignal = (GenerateCustomer) Generator.getTask("generating");
				tempGenerateSignal.setInterval(predictTime);
				tasks.takeTaskIn(tempGenerateSignal);

				if (timerLock.tryLock()) {
					try {
						monitor.schedule(tempGenerateSignal, predictTime);
						reporter.generatingLog(tempGenerateSignal.toString() + predictTime);
					} finally {
						timerLock.unlock();
					}
				}
				initialTime = -1;
				flag = false;
				// reporter.writeRepor("This is the " + ++counter + "th time
				// executing "
				// + "the signal() method, flag is: " + flag + ", initialTime
				// is: " + initialTime
				// + ". At time: " + StatisticalClock.CLOCK());
			}
		}
		monitor.cancel();

		return true;
	}

	public void systemShutDown() {
		System.out.println("over");
		System.exit(1);
	}

	public boolean start() {
		setUp();
		System.out.println("reset clock");
		clock = new StatisticalClock();
		initialTime = clock.CLOCK();
		System.out.println(initialTime);
		reporter.generateLogWriter();
		simulator.setUp();
		simulator.modify();
		// System.out.println("ok");
		// boolean starts = simulator.specialSituation_SystemFull();
		
		// reporter.writeRepor(
		// "Simulator starts at: " + StatisticalClock.CLOCK() + ", ending time
		// is: " + this.endingTime);
		// boolean ok = false;

		return signals();

	}
}
