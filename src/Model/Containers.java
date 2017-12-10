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
abstract class Containers {
	protected Collection container;
	protected String type;
	protected int ID;
	
	protected abstract boolean takeTaskIn(Task e);
	protected abstract Task popTaskOut();
	protected int getState() {
		return container.size();
	}
	
	protected String getName() {
		return type + ID;
	}
	
	public String toString() {
		return type + ID;
	}
	
	protected abstract int getSize();

}
