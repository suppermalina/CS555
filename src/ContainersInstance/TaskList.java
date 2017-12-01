/**
 * 
 */
package ContainersInstance;

import java.util.*;

import Model.Containers;
import Model.Task;
import Model.Task;

/**
 * @author mali
 *
 */
public class TaskList extends Containers {
	public TaskList(String type, int ID) {
		super(type, ID);
		// TODO Auto-generated constructor stub
	}
	private PriorityQueue<Task> Tasklist = new PriorityQueue<Task>(container);
	
	protected synchronized boolean isFull() {
		return Tasklist.size() > 0;
	}

	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	protected synchronized void takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		Tasklist.offer(e);

	}

	/* (non-Javadoc)
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	protected synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return Tasklist.poll();
	}

}
