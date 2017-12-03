/**
 * 
 */
package ContainersInstance;

import java.util.*;
import java.util.Map.Entry;

import EventsInstances.Customer;
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
	private Deque<Task> queue;
	public Queueing() {
		this.type = "QUEUE";
		this.ID = queueID++;
		queue = new LinkedList<Task>();
		System.out.println("Queue is ready");
	}

	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public synchronized boolean takeTaskIn(Task e) {
		// TODO Auto-generated method stub
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
