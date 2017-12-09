/**
 * 
 */
package ContainersInstance;

import java.util.*;
import java.util.Map.Entry;

import EventsInstances.Customer;
import ExecutionalInstances.Controller;
import ExecutionalInstances.ReportGenerator;
import ExecutionalInstances.StatisticalClock;
import Model.Containers;
import Model.Task;

/**
 * @author mali
 *
 */
public class Queueing extends Containers {
	public final int capacity = 5;
	private boolean checkIdle;
	private static int queueID = 1;
	private  Deque<Task> queue;
	private static Queueing instance = null;
	private Servers server = Servers.getInstance();

	public Queueing() {
		this.type = "QUEUE";
		this.ID = queueID++;
		queue = new LinkedList<Task>();
		System.out.println("Queue is ready");
	}

	public static synchronized Queueing getInstance() {
		if (instance == null) {
			instance = new Queueing();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public boolean takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		Customer tempCust = (Customer) e;
		// System.out.println("Queue tries to accept " + e.toString() + " at: "
		// + StatisticalClock.CLOCK());
		//Controller.reporter.queueLog("Queue tries to accept " + e.toString() + " at: " + Controller.clock.CLOCK());
		if (this.isFull()) {
			if (server.isIdle()) {
				server.takeIntoServer();
				if (!this.isFull()) {
					tempCust.setFlag();
					queue.offerLast(tempCust);
				}
				return true;
				//Controller.reporter.queueLog("Queue takes " + e.toString() + " at: " + Controller.clock.CLOCK());
			} else {
				//Controller.reporter.queueLog(tempCust.toString() + " is rejected at: " + Controller.clock.CLOCK());
				return false;
			}
		} else {
			tempCust.setFlag();
			queue.offerLast(tempCust);
			return true;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public Task popTaskOut() {
		// TODO Auto-generated method stub
		return queue.pollFirst();
	}

	public boolean isIdle() {
		return getSize() == 0;
	}

	public boolean isFull() {
		return getSize() >= this.capacity;
	}

	@Override
	public synchronized int getSize() {
		// TODO Auto-generated method stub
		return queue.size();
	}

}
