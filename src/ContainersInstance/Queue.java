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
public class Queue extends Containers {
	private Deque<Task> queue = new LinkedList<Task>(container);
	public Queue(String type, int ID) {
		super(type, ID);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	protected synchronized void takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		queue.offerLast(e);
	}

	/* (non-Javadoc)
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	protected synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return queue.pollFirst();
	}
	
	protected synchronized boolean isIdle() {
		return queue.isEmpty();
	}
	
	protected synchronized boolean isFull() {
		return queue.size() >= 5;
	}

}
