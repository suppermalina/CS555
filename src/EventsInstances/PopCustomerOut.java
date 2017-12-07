/**
 * 
 */
package EventsInstances;

import java.util.Timer;
import java.util.TimerTask;


import ContainersInstance.Servers;
import ExecutionalInstances.Controller;
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;
import Model.Observed;
import Model.Task;

/**
 * @author mali
 *
 */
public class PopCustomerOut extends Task {
	private int customerID;
	private int serverGoingToFire;
	// This is used to mark this object is the ith specific task being generated
	private static int popCounter = 1;

	public PopCustomerOut() {
		// A instanced controller here is a registered observer
		// Due to the controller class is a final class and the instance is
		// synchronized, so all signal tasks share the same instance
		this.type = "poping";
		this.id = popCounter++;
		this.initialTime = Controller.clock.CLOCK();
		Controller.tasks.takeTaskIn(this);
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
		return this.getTyp() + this.id + " is generated at: " + this.initialTime + ", and it will be executed at "
				+ this.terminalTime;
	}
	
	public void getServerID(int id) {
		this.serverGoingToFire = id;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Servers.servers[serverGoingToFire - 1].popTaskOut();
	}

}
