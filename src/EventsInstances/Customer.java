/**
 * 
 */
package EventsInstances;

import ExecutionalInstances.StatisticalClock;
import Model.*;

/**
 * @author mali
 *
 */
public class Customer extends Task {
	private boolean flag;
	private static int customerID = 1;
	private double enterServer;
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

	
	public double timeInServer() {
		if (flag) {
			return this.terminalTime - this.enterServer;
		} else {
			return 0.0;
		}
	}
	
	public double timeInQueue() {
		if (flag) {
			return this.enterServer - this.initialTime;
		} else {
			return 0.0;
		}
	}
	
	public void setTimeEnteringServer() {
		this.enterServer = StatisticalClock.CLOCK();
	}


}
