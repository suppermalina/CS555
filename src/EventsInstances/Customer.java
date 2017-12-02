/**
 * 
 */
package EventsInstances;

import ExecutionalInstances.Controller;
import ExecutionalInstances.StatisticalClock;
import Model.*;

/**
 * @author mali
 *
 */
public class Customer extends Task {
	private boolean flag;
	private static int customerID = 1;
	private double timeBeingPoped;
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public double timeInServer() {
		if (flag) {
			return this.timeBeingPoped - this.initialTime;
		} else {
			return 0.0;
		}
	}
	
	public void popedTime() {
		this.timeBeingPoped = StatisticalClock.CLOCK();
	}
	
	public double timeInQueue() {
		if (flag) {
			return this.enterServer - this.initialTime;
		} else {
			return 0.0;
		}
	}
	
	public void setTimeInServer() {
		this.enterServer = StatisticalClock.CLOCK();
	}


}
