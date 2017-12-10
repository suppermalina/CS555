/**
 * 
 */
package Model;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author mali
 *
 */
class Server extends Containers {
	private final int capacity = 1;
	private boolean isIdle;
	private boolean isFull;
	private int custID;
	// delay here is used to record the estimated service time
	private long delay;
	private static Integer serverID = 1;
	protected List<Task> server;
	private double miu = 1;
	private Timer timer;
	// private static Timer scheduler;
	private Lock timerLock;
	
	Server(int id) {
		this.type = "SERVER";
		this.ID = id;
		server = new ArrayList<Task>();
		timerLock = new ReentrantLock();
		timer = new Timer();
		// this.scheduler = new Timer();
		System.out.println("Server" + this.ID + "is ready");
	}

	protected synchronized boolean isFull() {
		return getSize() > 0;
	}

	protected boolean isIdle() {
		return getSize() == 0;
	}
	
	protected int signal = -1;
	
	protected void modify() {
		signal = 1;
	}
	
	protected boolean noCheating(int a) {
		if (a >= 0) {
			return false;
		} else return true;
	}
	
	/*
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	protected boolean takeTaskIn(Task e) {
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
			if (Controller.clock.CLOCK() >= Controller.endingTime) {
				temp.cancel();
				//timer.cancel();
				return true;
			} else {
				timer.schedule(temp, predictiTime + makeUpForCheating);
			}
			
		}
		return true;
	}

	/*
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	protected Task popTaskOut() {
		// TODO Auto-generated method stub
		if (server.isEmpty()) {
			return null;
		} else {
			Task t = server.remove(0);
			if (t.getTerminalTime() <= t.getInitialTime()) {
				t.cancel();
				t = null;
			}
			System.out.println(this.toString() + " pops " + t.toString());
			Servers.takeIntoServer();
			ModelSystem.customersOutFromServer(t);
			return t;
		}
		
	}

	@Override
	public synchronized int getSize() {
		// TODO Auto-generated method stub
		return server.size();
	}

}
