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
	private static int counterID = 1;
	public StatisticalCounter() {
		this.container = new ArrayList<Task>();
		this.type = "COUNTER";
		this.ID = counterID++;
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
