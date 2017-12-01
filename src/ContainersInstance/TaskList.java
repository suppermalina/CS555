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
	private static int taskListID = 1;
	public TaskList() {
		this.container = new PriorityQueue<Task>(10, new Comparator<Task>() {

			@Override
			public int compare(Task eOne, Task eTwo) {
				// TODO Auto-generated method stub
				double a = eOne.getInterval();
				double b = eTwo.getInterval();
				if (a < b) {
					return -1;
				} else if (a > b) {
					return 1;
				} else return 0;
			}
			
		});
		this.type = "TASKLIST";
		this.ID = taskListID++;
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
