/**
 * 
 */
package ContainersInstance;

import java.util.*;
import java.util.Map.Entry;

import EventsInstances.Customer;
import ExecutionalInstances.Controller;
import ExecutionalInstances.StatisticalClock;
import Model.Containers;
import Model.Task;

/**
 * @author mali
 *
 */
public class Queueing extends Containers {
	private final int capacity = 5;
	private boolean checkIdle;
	private static int queueID = 1;
	private Controller controller;
	// Deuqe provides convenient methods to implement the FIFO principle
	private Deque<Task> queue;
	private Controller contoller;
	private static int counter = 0;
	public Queueing() {
		this.type = "QUEUE";
		this.ID = queueID++;
		controller = Controller.getInstance();
		queue = new LinkedList<Task>();
		System.out.println("Queue is ready");
	}

	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public synchronized boolean takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		System.out.println(e + "in queue");
		controller.writeLog("Queue takes " + e.toString() + " at: " + StatisticalClock.CLOCK());
		return queue.offerLast(e);
	}

	/* (non-Javadoc)
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return queue.pollFirst();
	}
	
	public synchronized boolean isIdle() {
		return queue.size() == 0;
	}
	
	public synchronized boolean isFull() {
		return queue.size() >= 5;
	}
	
	public synchronized int firstCustID() {
		return queue.peek().getId();
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return queue.size();
	}

}
