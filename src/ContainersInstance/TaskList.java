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
	public  boolean takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		System.out.println("The " + counter++ + "th " + e.getTyp() + " task into tasklist");
		return tasklist.offer(e);

	}

	public boolean popTaskOut(Task t) {
		// TODO Auto-generated method stub
		if (!tasklist.isEmpty() && tasklist.peek().equals(t)) {
			Controller.writeLog(t.toString() + " is removed from tasklist at " + StatisticalClock.CLOCK());
			Controller.localLogRecorder.offerLast(tasklist.poll());
			return true;
		} else {
			Controller.writeLog(t.toString() + " is the task need to be removed");
			Controller.writeLog(t.getTimeInform());
			Controller.writeLog(tasklist.peek().toString() + " is the one on the top");
			Controller.writeLog(tasklist.peek().getTimeInform());
			if (tasklist.peek().getTerminalTime() < t.getTerminalTime()) {
				Controller.writeLog(tasklist.peek().toString() + " is going to be deleted by cheating");
				tasklist.poll().getLocalTask().cancel();
			}
			return true;
		}
	}

	@Override
	public synchronized int getSize() {
		// TODO Auto-generated method stub
		return tasklist.size();
	}

	@Override
	public Task popTaskOut() {
		// TODO Auto-generated method stub
		return null;
	}

}
