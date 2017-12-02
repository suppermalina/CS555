/**
 * 
 */
package ContainersInstance;

import java.util.*;

import Model.Containers;
import Model.Task;

/**
 * @author mali
 *
 */
public class Queueing extends Containers {
	private Deque<Task> queue = new LinkedList<Task>(container);
	private static int queueID = 1;
	public Queueing() {
		this.container = new LinkedList<Task>();
		this.type = "QUEUE";
		this.ID = queueID++;
	}

	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public
	synchronized void takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		queue.offerLast(e);
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
		return queue.isEmpty();
	}
	
	public synchronized boolean isFull() {
		return queue.size() >= 5;
	}
	
	public synchronized int firstCustID() {
		return queue.peek().getId();
	}

}
