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
	private PriorityQueue<Task> tasklist;
	public TaskList() {
		this.tasklist = new PriorityQueue<Task>(10, new Comparator<Task>() {

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
		System.out.println("TaskList is ready");
	}
	
	public boolean isEmpty() {
		return tasklist.isEmpty();
	}
	
	public synchronized boolean isFull() {
		return tasklist.size() > 0;
	}
	private int counter = 1;
	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public synchronized boolean takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		System.out.println("The " + counter++ + "th customer into tasklist");
		return tasklist.offer(e);

	}

	/* (non-Javadoc)
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return tasklist.poll();
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return tasklist.size();
	}

}
