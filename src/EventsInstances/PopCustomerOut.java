/**
 * 
 */
package EventsInstances;

import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.Controller;
import ExecutionalInstances.RandomNumberGenerator;
import Model.Observed;
import Model.Task;

/**
 * @author mali
 *
 */
public class PopCustomerOut extends Task implements Observed {
	private int targetCustID;
	private static int popCounter = 1;
	private double miu;
	private Timer timer;
	private Controller controller;
	private int idOfCusBeingPoped;
	private void setInterval() {
		this.interval = RandomNumberGenerator.getInstance(miu);
	}
	public PopCustomerOut() {
		this.type = "poping";
		this.id = popCounter++;
		this.timer = new Timer();
	}
	private class ServiceTimeTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	public boolean hasChanged() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void addController(Controller o) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyController(Task e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void setPopedCustomerID(int id) {
		this.idOfCusBeingPoped = id;
	}
	
	public int getPopedCustomerID() {
		return this.idOfCusBeingPoped;
	}
	
	public void markTargetID(int id) {
		this.targetCustID = id;
	}
	
	public int getTargetID() {
		return this.targetCustID;
	}

}
