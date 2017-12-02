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
	private static int stateListID = 1;
	public StateList() {
		this.container = new ArrayList<Task>();
		this.type = "STATELIST";
		this.ID = stateListID++;
	}
	
	public synchronized boolean isEmpty() {
		return statelist.isEmpty();
	}

	private Deque<Task> statelist = new LinkedList<Task>(container);
	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public synchronized void takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		statelist.offerLast(e);
	}

	/* (non-Javadoc)
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return statelist.pollFirst();
	}

}
