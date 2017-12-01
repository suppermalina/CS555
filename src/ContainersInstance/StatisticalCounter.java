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
public class StatisticalCounter extends Containers {
	public StatisticalCounter(String type, int ID) {
		super(type, ID);
		// TODO Auto-generated constructor stub
	}

	private Deque<Task> counter = new LinkedList<Task>()	;

	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	protected synchronized void takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		counter.offerLast(e);
	}

	/* (non-Javadoc)
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	protected synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return counter.pollLast();
	}
	
	protected synchronized boolean isEmpty() {
		return counter.isEmpty();
	}

}
