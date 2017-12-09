/**
 * 
 */
package EventsInstances;


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
	private Server temp = null;
	/**
	 * @param type
	 * @param id
	 */
	public Customer() {
		this.type = "CUSTOMER";
		this.id = customerID++;
		this.initialTime = Controller.clock.CLOCK();
		Controller.reporter.generatingLog(this.toString() + " was created at " + this.initialTime);
	}

	// If the customer is accepted by the system, then the flag = true
	// If it's rejected, the flag remains false
	public void setFlag() {
		flag = true;
	}
	
	public void timeEnterServer() {
		this.enterServer = Controller.clock.CLOCK();
	}

	public void setTerminalTime() {
		this.terminalTime = Controller.clock.CLOCK();
	}

	// This method is used to investigate the average time being spent
	// in the system by each accepted customer
	public long timeInSystem() {
		if (flag) {
			return this.terminalTime - this.initialTime;
		} else {
			return 0;
		}
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
		this.enterServer = Controller.clock.CLOCK();
	}
	public void getServerTrigger(Server s) {
		System.out.println("temp hold by " + this.toString() + " takes " + s.toString());
		temp = s;
	}
	public String toString() {
		return this.type + this.id;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		setTerminalTime();
		temp.popTaskOut();
	}
}
