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

import ContainersInstance.ModelSystem;
import ContainersInstance.Server;
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
	public static double endingTime = 50000.0;
	public static double period = 1000.0;
	
	// This flag is used to inform the GenerateCustomer
	public static boolean flag = true;
	private Lock timerLock;

	// These two lists were in the Simulator class
	public static ReportGenerator reporter;
	public static long initialTime;
	public static StatisticalClock clock;
	public static Timer monitor;
	private Lock lock;
	public static ModelSystem model;
	// public static Simulator simulator;

	Controller() {
		System.out.println("Controller ready");
	}

	private void setUp() {
		reporter = new ReportGenerator();
		monitor = new Timer();
		timerLock = new ReentrantLock();
		lock = new ReentrantLock();
		model = new ModelSystem();
		// simulator = new Simulator();
	}

	private static Controller instance = null;;

	public static synchronized Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
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
		// int counter = 0;
		while (clock.CLOCK() <= endingTime) {
			// long time = clock.NANOCLOCK();
			// long nanoPoint = TimeUnit.NANOSECONDS.toNanos(5000);
			/*if (time % nanoPoint == 0) {

				//reporter.writeRepor("--------------------------------------------------------------------");
				//reporter.writeRepor("There are total " + simulator.currentState() + " customers in the system at: "
					//	+ clock.CLOCK());
				//reporter.writeRepor("--------------------------------------------------------------------");

				simulator.plotState(time);
				// simulator.dataForPloting(time);
				// time = -1;

			}*/

			if (initialTime == 0 || flag == true) {
				// predictTime here is used for generating the coming new
				// customer
				long predictTime = (long) (RandomNumberGenerator.getInstance(lambda) * 1000);
				// The task should be sent to the tasklist
				GenerateCustomer tempGenerateSignal = new GenerateCustomer();
				tempGenerateSignal.setInterval(predictTime);

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
		model.modify();
		// System.out.println("ok");
		// boolean starts = simulator.specialSituation_SystemFull();
		
		// reporter.writeRepor(
		// "Simulator starts at: " + StatisticalClock.CLOCK() + ", ending time
		// is: " + this.endingTime);
		// boolean ok = false;
		model.setTimeTask();
		return signals();

	}
}
