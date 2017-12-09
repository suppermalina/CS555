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
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.ReportGenerator;
import ExecutionalInstances.StatisticalClock;
import Model.Containers;
import Model.Task;


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
	private Timer timer;
	// private static Timer scheduler;
	private Lock timerLock;
	
	public Server(int id) {
		this.type = "SERVER";
		this.ID = id;
		server = new ArrayList<Task>();
		timerLock = new ReentrantLock();
		timer = new Timer();
		// this.scheduler = new Timer();
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
	public boolean takeTaskIn(Task e) {
		if (getSize() == 0) {
			// TODO Auto-generated method stub
			Customer temp = (Customer) e;
			temp.timeEnterServer();
			boolean cheating = noCheating(signal);
			//Controller.reporter.serverLog(this.toString() + " takes " + e.toString() + " at: " + Controller.clock.CLOCK());
			System.out.println(this.toString() + " takes " + e.toString() + " at: " + Controller.clock.CLOCK());
			System.out.println(this.toString());
			temp.getServerTrigger(this);
			server.add(temp);
			// Once a customer was accepted by any one of the servers, then a
			// poping signal
			// task should be generated immediately
			long predictiTime = (long) (RandomNumberGenerator.getInstance(miu) * 1000);
			//Controller.reporter.serverLog(this.toString() + " assigns " + e.toString() + " predictied service time: "
				//	+ Controller.clock.CLOCK());

			long makeUpForCheating = 0;
			if (cheating) {
				makeUpForCheating = 250;
			} else {
				makeUpForCheating = 0;
			}
			timer.schedule(temp, predictiTime);
			// The task should be sent to the tasklist
			PopCustomerOut tempPop = new PopCustomerOut();
			tempPop.markTargetID(temp.getId());
			tempPop.setInterval(predictiTime);
			//Controller.tasks.takeTaskIn(tempPop);
			if (cheating) {
				//Controller.reporter.writeRepor(
					//	"******************************Earily bird is waiting in the server**********************************");
			}
			//Controller.reporter.serverLog(
				//	"******************************cheating in " + this.toString() + " is " + cheating + "**********************************");
			tempPop.getServerID(this.ID);
		}
		return true;
	}

	/*
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public Task popTaskOut() {
		// TODO Auto-generated method stub
		Task t = server.remove(0);
		System.out.println(this.toString() + " pops " + t.toString());
		//Controller.reporter.popingLog(
		//		this.toString() + " popps " + t.toString() + " at: " + Controller.clock.CLOCK());
		//Controller.counter.takeTaskIn(t);
		Servers.takeIntoServer();
		ModelSystem.customersOutFromServer(t);
		return t;
	}

	@Override
	public synchronized int getSize() {
		// TODO Auto-generated method stub
		return server.size();
	}

}
