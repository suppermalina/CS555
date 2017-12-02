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
				double a = eOne.getTerminalTime();
				double b = eTwo.getTerminalTime();
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
	
	public synchronized boolean isFull() {
		return Tasklist.size() > 0;
	}

	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public synchronized boolean takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		return Tasklist.offer(e);

	}

	/* (non-Javadoc)
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return Tasklist.poll();
	}

}
