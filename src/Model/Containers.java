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
	protected Collection container;
	protected String type;
	protected int ID;
	
	public abstract boolean takeTaskIn(Task e);
	public abstract Task popTaskOut();
	public int getState() {
		return container.size();
	}
	
	public String getName() {
		return type + ID;
	}
	
	public abstract int getSize();

}
