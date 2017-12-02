/**
 * 
 */
package EventsInstances;

import ExecutionalInstances.Coordinator;
import ExecutionalInstances.StatisticalClock;
import Model.*;

/**
 * @author mali
 *
 */
public class Customer extends Task {
	private boolean flag;
	private static int customerID = 1;
	/**
	 * @param type
	 * @param id
	 */
	public Customer() {
		this.type = "customer";
		this.id = customerID++;
		this.initialTime = StatisticalClock.CLOCK();
	}
	
	public void setFlag() {
		flag = true;
	}
	
	public void setTerminalTime() {
		this.terminalTime = StatisticalClock.CLOCK();
	}
	

	public double timInSystem() {
		return this.terminalTime - this.initialTime;
	}
	
	public String toString() {
		return this.type + this.id;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}


}
