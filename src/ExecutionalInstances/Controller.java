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

import ContainersInstance.StateList;
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
	private double lambda = 0.5;
	// The service rate
	private double miu = 1;
	private long endingTime = 10000;
	private long delay;
	public static Deque<Task> localLogRecorder;

	private static int massage = 0;
	private boolean flag = false;

	// These two lists were in the Simulator class
	public static StateList log;
	public static TaskList tasks;
	private static Lock lock;

	private Controller() {
		System.out.println("Controller ready");
	}

	private void setUp() {
		// This localLogRecorder is initiated for recording the timeline for
		// each task
		// When it is generated, and when it is excuted
		// Key is the information of the task, value is the time such an
		// execution happens
		localLogRecorder = new LinkedList<Task>();

		log = (StateList) Generator.getContainer("statelist");
		tasks = (TaskList) Generator.getContainer("tasklist");
		lock = new ReentrantLock();
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

	public static void listener(int massage) {
		Controller.massage = massage;
	}

	private boolean doit() {
		return massage == 1;
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

	public void exportLog() {
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

	//private int counter = 1;

	// This method is used to generate signals for generating new customer
	private boolean signals() {
		long initialTime = StatisticalClock.CLOCK();
		// Test.averageSignalInitiatingTime += initialTime;
		writeLog("signals() was started at: " + initialTime);
		while (StatisticalClock.CLOCK() <= endingTime) {
			//this.writeLog("This is the counter " + counter++ + "th loop in the signals loop");
			// A generating signal should be generated once at the very
			// beginning
			// or a new customer is generated
			//this.writeLog("Now the flag is " + flag + " before doit()");
			flag = doit();
			//this.writeLog("Now the flag is " + flag + " after doit()");
			// this.writeLog("Now the flag is " + flag + " after receiver()");
			if (initialTime == 0 || flag == true) {
				delay = (long) (RandomNumberGenerator.getInstance(lambda) * 1000);
				Generator.intervalForGenerating = delay;
				GenerateCustomer tempCust = (GenerateCustomer) Generator.getTask("generating");
				// System.out.println("Is this gener " + tempCust.getId() + " +
				// " + tempCust.getTerminalTime());
				tasks.takeTaskIn(tempCust);
				String information = tempCust.toString() + " being generated at :";
				// System.out.println(tasks.getSize());
				// System.out.println(information + StatisticalClock.CLOCK());
				this.writeLog(information + StatisticalClock.CLOCK());
				initialTime = -1;
				flag = false;
			}
			System.out.println("generating signal skips at: " + StatisticalClock.CLOCK());
		}
		while (StatisticalClock.CLOCK() > this.endingTime) {
			System.out.println(log);
			System.out.println("Over");
			System.exit(1);
			return true;
		}
		return false;
	}

	public static void notified(Task t) {
		if (lock.tryLock()) {
			writeLog(t.toString() + " is holding the ReentrantLock in notified() at: " + StatisticalClock.CLOCK());
			try {
				if (t == null) {
					System.out.println("it's null");
				} else {
					System.out.println("center received the task");
				}

				// Once a signal task informs the controller that it reaches the
				// due
				// time, it has to be popped out from the tasklist
				System.out.println("Task " + t.getTyp() + " update at: " + StatisticalClock.CLOCK());
				//this part used to be update
				if (t instanceof GenerateCustomer) {
					writeLog("Generating signal from " + t.toString() + " at: " + StatisticalClock.CLOCK());
					simulator.generateNewCustomer(t);
				} else {
					writeLog("Poping signal from " + t.toString() + " at: " + StatisticalClock.CLOCK());
					simulator.popCustomer((PopCustomerOut) t);
				}
				SystemState currentState = new SystemState(StatisticalClock.CLOCK(), simulator.currentState());
				log.takeStateIn(currentState);
			} finally {
				writeLog(t.toString() + " is going to unlock the ReentrantLock at: " + StatisticalClock.CLOCK());
				lock.unlock();
			}
		} else {
			writeLog(t.toString() + " is waiting for the ReentrantLock in notified at: " + StatisticalClock.CLOCK());
		}
	}

	public boolean start() {
		setUp();
		simulator.setUp();
		generateLogWriter();
		writeLog("Simulator starts at: " + StatisticalClock.CLOCK() + ", ending time is: " + this.endingTime);
		return signals();
	}
}
