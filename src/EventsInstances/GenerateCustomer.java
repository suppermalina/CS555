/**
 * 
 */
package EventsInstances;

import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ExecutionalInstances.Controller;
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.Simulator;
import ExecutionalInstances.StatisticalClock;
import ExecutionalInstances.TimerForLocal;
import Model.Observed;
import Model.Task;


/**
 * @author mali
 *
 */
public class GenerateCustomer extends Task implements Job {
	// This is used to mark this object is the ith specific task being generated
	private static int generateCustomerID = 1;
	private Simulator access;

	// Supposed to have a controller, but not sure. May be it will be removed
	// later
	private int customerID;
	public void setInterval(long interval) {
		// interval is the estimated interval between a new generated task and
		// the next
		// task which supposed to be generated
		this.interval = interval;

		// Tasks in the tasklist are sorted based on the terminalTime
		this.terminalTime = this.interval + this.initialTime;
	}
	
	public void markCustomerID(int id) {
		this.customerID = id;
	}

	public GenerateCustomer() {
		// A instanced controller here is a registered observer
		// Due to the controller class is a final class and the instance is
		// synchronized, so all signal tasks share the same instance
		org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");

		this.access = Simulator.getInstance();
		this.initialTime = StatisticalClock.CLOCK();
		this.type = "generating";
		this.id = generateCustomerID++;
	}

	public String toString() {
		return "Customer" + customerID + " is send into server at: " + this.initialTime
				+ " will end at " + this.terminalTime;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		access.generateNewCustomer(this);
		setFlag();
	}
	
	public void setFlag() {
		Controller.flag = true;
	}
	

}
