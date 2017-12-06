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
	// Deuqe provides convenient methods to implement the FIFO principle
	private static Deque<Task> queue;
	private Controller contoller;
	private static int counter = 0;
	private static Queueing instance = null;
	private Servers server = Servers.getInstance();
	public Set<Customer> rejected;

	public Queueing() {
		this.type = "QUEUE";
		this.ID = queueID++;
		rejected = new HashSet<Customer>();
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
	public void takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		Customer tempCust = (Customer) e;
		// System.out.println("Queue tries to accept " + e.toString() + " at: "
		// + StatisticalClock.CLOCK());
		Controller.reporter.queueLog("Queue tries to accept " + e.toString() + " at: " + StatisticalClock.CLOCK());
		if (this.isFull()) {
			if (server.isIdle()) {
				server.takeIntoServer();
				if (!this.isFull()) {
					tempCust.setFlag();
					queue.offerLast(tempCust);
				}
				Controller.reporter.queueLog("Queue takes " + e.toString() + " at: " + StatisticalClock.CLOCK());
			} else {
				Controller.reporter.queueLog(tempCust.toString() + " is rejected at: " + StatisticalClock.CLOCK());
				rejected.add(tempCust);
			}
		} else {
			tempCust.setFlag();
			queue.offerLast(tempCust);
			Controller.reporter.queueLog(queue.peekFirst().toString() + " on the head at: " + StatisticalClock.CLOCK());
			Controller.reporter.queueLog("Queue takes " + e.toString() + " at: " + StatisticalClock.CLOCK());
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
