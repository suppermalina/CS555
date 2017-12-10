package Model;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

import Model.*;

class Controller {
	private double lambda = 0.5;
	protected static double endingTime = 60000.0;
	protected static double period = 500.0;
	
	// This flag is used to inform the GenerateCustomer
	protected static boolean flag = true;
	protected Lock timerLock;

	protected static long initialTime;
	protected static StatisticalClock clock;
	protected Timer monitor;
	private Lock lock;
	protected static ModelSystem model;
	// public static Simulator simulator;

	Controller() {
		System.out.println("Controller ready");
	}

	private void setUp() {
		monitor = new Timer();
		timerLock = new ReentrantLock();
		lock = new ReentrantLock();
		model = new ModelSystem();	}

	private static Controller instance = null;;

	protected static synchronized Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	private boolean signals() {
		System.out.println("signals() was started at: " + initialTime);
		while (clock.CLOCK() <= endingTime) {

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
					} finally {
						timerLock.unlock();
					}
				}
				initialTime = -1;
				flag = false;
			}
		}
		model.cleanUnexecutedTasks();
		monitor.cancel();
		
		return true;
	}

	protected void systemShutDown() {
		System.out.println("over");
		System.exit(1);
	}

	protected boolean start() {
		setUp();
		System.out.println("reset clock");
		clock = new StatisticalClock();
		initialTime = clock.CLOCK();
		System.out.println(initialTime);
		
		//boolean starts = model.specialSituation_PartialFull(4);
		//if (starts) {
			model.modify();
		//}
		
		model.setTimeTask();
		
		return signals();
	}
}
