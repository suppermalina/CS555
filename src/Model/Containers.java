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
	protected String type;
	protected int ID;
	
	protected abstract void takeTaskIn(Task e);
	protected abstract Task popTaskOut();
	protected int getState() {
		return container.size();
	}
	
	protected String getName() {
		return type + ID;
	}

}
