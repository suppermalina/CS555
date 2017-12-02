/**
 * 
 */
package ContainersInstance;

import java.util.*;

import Model.Containers;
import Model.SystemState;
import Model.Task;
/**
 * @author mali
 *
 */
public class StateList extends Containers {
	private static int stateListID = 1;
	public StateList() {
		this.type = "STATELIST";
		this.ID = stateListID++;
	}
	
	public synchronized boolean isEmpty() {
		return statelist.isEmpty();
	}

	public Deque<SystemState> statelist = new LinkedList<SystemState>();
	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public synchronized boolean takeTaskIn(Task t) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public synchronized SystemState popStateOut() {
		// TODO Auto-generated method stub
		return statelist.pollFirst();
	}
	
	private class State {
		
	}

	public void takeStateIn(SystemState state) {
		// TODO Auto-generated method stub
		statelist.offerLast(state);
	}


}
