/**
 * 
 */
package ContainersInstance;

import java.util.*;

import ExecutionalInstances.Controller;
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
		System.out.println("StateList is ready");
	}
	
	public synchronized boolean isEmpty() {
		return statelist.isEmpty();
	}

	public Deque<SystemState> statelist = new LinkedList<SystemState>();
	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public synchronized void takeTaskIn(Task t) {
		// TODO Auto-generated method stub
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
	
	private int counter = 1;
	public void takeStateIn(SystemState state) {
		// TODO Auto-generated method stub
		//Controller.writeLog("statelist takes " + counter++ + "th state");
		statelist.offerLast(state);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return statelist.size();
	}


}
