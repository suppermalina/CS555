/**
 * 
 */
package ContainersInstance;

import java.util.*;

import EventsInstances.GenerateCustomer;
import EventsInstances.PopCustomerOut;
import ExecutionalInstances.Controller;
import ExecutionalInstances.StatisticalClock;
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
				long a = eOne.getTerminalTime();
				long b = eTwo.getTerminalTime();
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
	
	public Task peek() {
		return tasklist.peek();
	}
	
	public boolean isEmpty() {
		return tasklist.isEmpty();
	}
	
	public boolean isFull() {
		return tasklist.size() > 0;
	}
	private int counter = 1;
	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public  void takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		//System.out.println("The " + counter++ + "th " + e.getTyp() + " task into tasklist");
		Controller.reporter.writeLog(this.toString() + " takes such an information: " + e.toString());
		tasklist.offer(e);
	}


	@Override
	public synchronized int getSize() {
		// TODO Auto-generated method stub
		return tasklist.size();
	}

	@Override
	public Task popTaskOut() {
		// TODO Auto-generated method stub
		return tasklist.poll();
	}

}
