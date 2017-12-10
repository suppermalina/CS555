/**
 * 
 */
package Model;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author mali
 *
 */
class GenerateCustomer extends Task {
	// This is used to mark this object is the ith specific task being generated
	private static int generateCustomerID = 1;
	// private Simulator access = Simulator.getInstance();


	protected void setInterval(long interval) {
		// interval is the estimated interval between a new generated task and
		// the next
		// task which supposed to be generated
		this.interval = interval;

		// Tasks in the tasklist are sorted based on the terminalTime
		this.terminalTime = this.interval + this.initialTime;
	}

	GenerateCustomer() {
		// log4j is required by the quartz lib
		//org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");
		// A instanced controller here is a registered observer
		// Due to the controller class is a final class and the instance is
		// synchronized, so all signal tasks share the same instance
		this.initialTime = Controller.clock.CLOCK();
		this.type = "generating";
		this.id = generateCustomerID++;
	}

	public String toString() {
		return this.type + generateCustomerID + " is generated at: " + this.initialTime + " will be executed at: "
				+ this.terminalTime + " with interval";
	}


	protected void setFlag() {
		Controller.flag = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Controller.model.generateNewCustomer(this);
		setFlag();
	}

}
