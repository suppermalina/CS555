/**
 * 
 */
package ContainersInstance;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



import EventsInstances.Customer;
import EventsInstances.PopCustomerOut;
import ExecutionalInstances.Controller;
import ExecutionalInstances.Generator;
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.ReportGenerator;
import ExecutionalInstances.StatisticalClock;
import Model.Containers;
import Model.Task;
import TestQuatz.TaskSchedule;

/**
 * @author mali
 *
 */
public class Server extends Containers {
	private final int capacity = 1;
	private boolean isIdle;
	private boolean isFull;
	private int custID;
	// delay here is used to record the estimated service time
	private long delay;
	private static Integer serverID = 1;
	private List<Task> server;
	private double miu = 1;

	// private static TriggerBuilder tirgger = null;
	private static long index = 1;
	private static Timer scheduler;
	private Lock timerLock;
	public Server() {
		this.type = "SERVER";
		this.ID = this.serverID++;
		server = new ArrayList<Task>();
		timerLock = new ReentrantLock();
		this.scheduler = new Timer();
		System.out.println("Server" + this.ID + "is ready");
	}

	public synchronized boolean isFull() {
		return getSize() > 0;
	}

	public boolean isIdle() {
		return getSize() == 0;
	}
	
	private int signal = -1;
	
	public void modify() {
		signal = 1;
	}
	
	private boolean noCheating(int a) {
		if (a >= 0) {
			return false;
		} else return true;
	}
	
	/*
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public void takeTaskIn(Task e) {
		
		if (getSize() == 0) {
			// TODO Auto-generated method stub
			Customer temp = (Customer) e;
			boolean cheating = noCheating(signal);
			Controller.writeLog(this.toString() + " takes " + e.toString() + " at: " + StatisticalClock.CLOCK());
			System.out.println(this.toString() + " takes " + e.toString() + " at: " + StatisticalClock.CLOCK());
			System.out.println(this.toString());
			temp.getServerTrigger(this);
			server.add(temp);
			// Once a customer was accepted by any one of the servers, then a
			// poping signal
			// task should be generated immediately
			long predictiTime = (long) (RandomNumberGenerator.getInstance(miu) * 1000);
			Controller.writeLog(this.toString() + " assigns " + e.toString() + " predictied service time: "
					+ StatisticalClock.CLOCK());

			long makeUpForCheating = 0;
			if (cheating) {
				makeUpForCheating = 250;
			} else {
				makeUpForCheating = 0;
			}

			// The task should be sent to the tasklist
			PopCustomerOut tempPop = new PopCustomerOut();
			tempPop.markTargetID(temp.getId());
			tempPop.setInterval(predictiTime);
			Controller.tasks.takeTaskIn(tempPop);
			if (cheating) {
				Controller.writeLog(
						"******************************Earily bird is waiting in the server**********************************");
			}
			Controller.writeLog(
					"******************************cheating in " + this.toString() + " is " + cheating + "**********************************");
			tempPop.getServerID(this.ID);
			
			if (timerLock.tryLock()) {
				try {
					Controller.monitor.schedule(tempPop, makeUpForCheating + predictiTime);
				} finally {
					timerLock.unlock();
				}
			}
		}
	}

	/*
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public Task popTaskOut() {
		// TODO Auto-generated method stub
		Task t = server.remove(0);
		Controller.writeLog(
				this.toString() + " popps " + t.toString() + " predictied service time: " + StatisticalClock.CLOCK());
		Controller.counter.takeTaskIn(t);
		Servers.takeIntoServer();
		return t;
	}

	@Override
	public synchronized int getSize() {
		// TODO Auto-generated method stub
		return server.size();
	}

}
