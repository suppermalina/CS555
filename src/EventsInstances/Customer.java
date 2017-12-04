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
	private long enterServer;

	/**
	 * @param type
	 * @param id
	 */
	public Customer() {
		this.type = "customer";
		this.id = customerID++;
		this.initialTime = StatisticalClock.CLOCK();
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

}
