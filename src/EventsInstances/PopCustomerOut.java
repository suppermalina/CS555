/**
 * 
 */
package EventsInstances;

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
public class PopCustomerOut extends Task implements Observed {
	private int targetCustID;
	
	// This is used to mark this object is the ith specific task being generated
	private static int popCounter = 1;
	
	// idInTaskList is used to find this task object in task list. It will be
	// stored as the key value into a hash-map with the object as the value 
	private int idInTaskList;
	
	// The service rate
	private double miu;
	private Timer timer;
	// Supposed to have a controller, but not sure. May be it will be removed later
	private Center controller;
	private void setInterval(long interval) {
		// interval is the estimated service time for a task in a server 
		this.interval = (long) RandomNumberGenerator.getInstance(miu);
		this.terminalTime = (long) (this.initialTime + this.interval);
	}
	public PopCustomerOut(long interval) {
		controller = Center.getInstance();
		this.type = "poping";
		this.id = popCounter++;
		this.idInTaskList = this.idForTaskList++;
		this.initialTime = StatisticalClock.CLOCK();
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
	public void addController(Center o) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public synchronized void notifyController() {
		System.out.println("pop notify");
		controller.notified(this);
	}
	
	public int getPopedCustomerID() {
		return this.targetCustID;
	}
	
	public void markTargetID(int id) {
		this.targetCustID = id;
	}
	
	public int getTargetID() {
		return this.targetCustID;
	}
	
	public int getIdInTaskList() {
		return this.idInTaskList;
	}
	
	private class LocalClock extends TimerTask {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// Once reaches the termination time, notify the controller by returning itself
			notifyController();
		}
		
	}

}
