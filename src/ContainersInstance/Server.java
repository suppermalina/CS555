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
public class Server extends Containers {

	private static Integer serverID = 1;
	private List<Task> server = new ArrayList<Task>(container);
	private double miu;
	
	public Server() {
		this.container = new LinkedList<Task>();
		this.type = "SERVER";
		this.ID = serverID++;
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
