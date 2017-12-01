/**
 * 
 */
package ContainersInstance;

import java.util.*;
import java.util.Collection;

import Model.Containers;
import Model.Task;

/**
 * @author mali
 *
 */
public class Server extends Containers {
	private List<Task> server = new ArrayList<Task>(container);
	
	public Server(String type, int ID) {
		super(type, ID);
	}

	public synchronized boolean isFull() {
		return container.size() >= 1;
	}
	
	public synchronized boolean isIdle() {
		return container.isEmpty();
	}

	/* 
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	protected synchronized void takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		server.add(e);
	}

	/* 
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	protected synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return server.get(0);
	}

}
