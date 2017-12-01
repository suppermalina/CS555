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
public class StateList extends Containers {
	public StateList(String type, int ID) {
		super(type, ID);
		// TODO Auto-generated constructor stub
	}
	
	protected synchronized boolean isEmpty() {
		return statelist.isEmpty();
	}

	private Deque<Task> statelist = new LinkedList<Task>(container);
	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	protected synchronized void takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		statelist.offerLast(e);
	}

	/* (non-Javadoc)
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	protected synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return statelist.pollFirst();
	}

}
