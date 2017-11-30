/**
 * 
 */
package Model;

import java.util.*;

/**
 * @author mali
 * If we take a consideration on queue, server, statelist and eventlist
 * we can easily notice that the can be implemented with the Collection
 * class provided by Java. This superclass contains all common fields in
 * those instances mentioned previously.
 */
public abstract class Containers {
	private Collection container;
	private String type;
	private int ID;
	
	abstract Collection generateContainer();
	abstract void takeEventIn(Event e);
	abstract Event popEventOut();
	int getState() {
		return container.size();
	}
	
	String getName() {
		return type + ID;
	}
	
	

}
