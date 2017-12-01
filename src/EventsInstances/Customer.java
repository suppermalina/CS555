/**
 * 
 */
package EventsInstances;

import Model.*;

/**
 * @author mali
 *
 */
public class Customer extends Task {
	private boolean flag;
	/**
	 * @param type
	 * @param id
	 */
	public Customer(String type, int id) {
		super(type, id);
		// TODO Auto-generated constructor stub
	}
	
	public void setFlag() {
		flag = true;
	}
	
	public String toString() {
		return this.type + this.id;
	}


}
