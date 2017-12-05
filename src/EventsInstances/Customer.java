/**
 * 
 */
package EventsInstances;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ContainersInstance.Server;
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
	private long enterServer;
	public boolean trigger;
	private Server temp = null;
	/**
	 * @param type
	 * @param id
	 */
	public Customer() {
		org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");
		this.type = "customer";
		this.id = customerID++;
		this.initialTime = StatisticalClock.CLOCK();
		Controller.writeLog(this.toString() + " was created at " + this.initialTime);
	}

	// If the customer is accepted by the system, then the flag = true
	// If it's rejected, the flag remains false
	public void setFlag() {
		flag = true;
	}

	public void setTerminalTime() {
		this.terminalTime = StatisticalClock.CLOCK();
	}

	// This method is used to investigate the average time being spent
	// in the system by each accepted customer
	public long timInSystem() {
		return this.terminalTime - this.initialTime;
	}

	// This method is used to investigate the average service time being spent
	// in the system by
	// each accepted customer
	public long timeInServer() {
		if (flag) {
			return this.terminalTime - this.enterServer;
		} else {
			return 0l;
		}
	}

	// This method is used to investigate the average waiting time being spent
	// in the system by each accepted customer
	public long timeInQueue() {
		if (flag) {
			return this.enterServer - this.initialTime;
		} else {
			return 0l;
		}
	}

	public void setTimeEnteringServer() {
		this.enterServer = StatisticalClock.CLOCK();
	}
	public void getServerTrigger(Server s) {
		System.out.println("temp hold by " + this.toString() + " takes " + s.toString());
		temp = s;
	}
	public String toString() {
		return this.type + this.id;
	}
}
