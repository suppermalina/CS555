/**
 * 
 */
package Model;

import java.util.*;

/**
 * @author mali
 * If we take a consideration on queue, server, statelist and Tasklist
 * we can easily notice that the can be implemented with the Collection
 * class provided by Java. This superclass contains all common fields in
 * those instances mentioned previously.
 */
public abstract class Containers {
	protected Collection<Task> container;
	protected Deque<Task> list;
	private String type;
	private int ID;
	
	protected Containers(String type, int ID) {
		if (type.equalsIgnoreCase("server")) {
			this.list = new LinkedList<Task>();
		} else if (type.equalsIgnoreCase("queue")) {
			this.container = new LinkedList<Task>();
		} else if (type.equalsIgnoreCase("taklist")) {
			this.container = new PriorityQueue<Task>(10, new Comparator<Task>() {

				@Override
				public int compare(Task eOne, Task eTwo) {
					// TODO Auto-generated method stub
					long a = eOne.getInterval();
					long b = eTwo.getInterval();
					if (a < b) {
						return -1;
					} else if (a > b) {
						return 1;
					} else return 0;
				}
				
			});
		} else if (type.equalsIgnoreCase("statelist")) {
			this.container = new ArrayList<Task>();
		} else if (type.equals("counter")) {
			this.container = new ArrayList<Task>();
		} else {}
		this.type = type;
		this.ID = ID;
	}
	
	protected abstract void takeTaskIn(Task e);
	protected abstract Task popTaskOut();
	protected int getState() {
		return container.size();
	}
	
	protected String getName() {
		return type + ID;
	}

}
