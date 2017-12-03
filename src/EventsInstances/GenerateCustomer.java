/**
 * 
 */
package EventsInstances;

import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.Center;
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;
import Model.Observed;
import Model.Task;

/**
 * @author mali
 *
 */
public class GenerateCustomer extends Task implements Observed {
	
	
	// This is used to mark this object is the ith specific task being generated
	private static int generateCustomerID = 1;
	
	// idInTaskList is used to find this task object in task list. It will be
	// stored as the key value into a hash-map with the object as the value 
	private int idInTaskList;
	
	// Supposed to have a controller, but not sure. May be it will be removed later
	private Center controller;
	
	private Timer timer;
	
	public synchronized void setInterval(long interval) {
		// interval is the estimated interval between a new generated task and the next 
		// task which supposed to be generated
		this.interval = interval;
		this.terminalTime = (long) (this.interval + this.initialTime);
	}
	
	public GenerateCustomer(long interval) {
		controller = Center.getInstance();
		this.initialTime = StatisticalClock.CLOCK();
		this.type = "generating";
		this.id = generateCustomerID++;
		this.idInTaskList = this.idForTaskList++;
		this.setInterval(interval);
		timer = new Timer();
		timer.schedule(new LocalClock(), interval);
	}

	@Override
	public boolean hasChanged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized void addController(Center o) {
		// TODO Auto-generated method stub
	}

	@Override
	public synchronized void notifyController() {
		// TODO Auto-generated method stub
		System.out.println("Ok, the " + testCounter + " times notify");
		controller.notified(this);
	}
	
	public int getIdInTaskList() {
		return this.idInTaskList;
	}
	public static int testCounter = 1;
	private class LocalClock extends TimerTask {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// Once reaches the termination time, notify the controller by returning itself
			System.out.println("Test localclock " + testCounter++);
			notifyController();
		}
		
	}

}
