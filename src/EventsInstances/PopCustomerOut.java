/**
 * 
 */
package EventsInstances;

import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.Controller;
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;
import ExecutionalInstances.TimerForLocal;
import Model.Observed;
import Model.Task;

/**
 * @author mali
 *
 */
public class PopCustomerOut extends Task {
	private int customerID;
	// This is used to mark this object is the ith specific task being generated
	private static int popCounter = 1;

	public PopCustomerOut() {
		// A instanced controller here is a registered observer
		// Due to the controller class is a final class and the instance is
		// synchronized, so all signal tasks share the same instance
		org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");
		this.type = "poping";
		this.id = popCounter++;
		this.initialTime = StatisticalClock.CLOCK();
	}
	
	public void setInterval(long interval) {
		this.interval = interval;
		this.terminalTime = this.initialTime + this.interval;
	}

	public void markTargetID(int id) {
		this.customerID = id;
	}

	public int getTargetID() {
		return this.customerID;
	}
	
	public String toString() {
		return "Customer" + customerID + " is send into server at: " + this.initialTime
				+ " will end at " + this.terminalTime;
	}

}
