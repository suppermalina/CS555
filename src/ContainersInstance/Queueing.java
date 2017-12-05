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
	private static Deque<Task> queue;
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
	public synchronized void takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		System.out.println(e.toString() + " in queue");
		controller.writeLog("Queue takes " + e.toString() + " at: " + StatisticalClock.CLOCK());
		if (Servers.isIdle()) {
			Servers.takeQueue(this);;
			Controller.writeLog("size of queue is " + this.getSize());
			Controller.writeLog(e.toString() + " is going to the server " + StatisticalClock.CLOCK());
			Servers.takeIntoServer();
		} else {
			queue.offerLast(e);
		}
	}

	/* (non-Javadoc)
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return queue.pollFirst();
	}
	
	public static synchronized boolean isIdle() {
		return queue.isEmpty();
	}
	
	public static synchronized boolean isFull() {
		return queue.size() >= 5;
	}
	

	@Override
	public synchronized int getSize() {
		// TODO Auto-generated method stub
		return queue.size();
	}

}
