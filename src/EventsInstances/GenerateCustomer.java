/**
 * 
 */
package EventsInstances;

import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;
import Model.Task;

/**
 * @author mali
 *
 */
public class GenerateCustomer extends Task {
	private double duration;
	private double lambda;
	private static int generateCustomerID = 1;
	private Timer timer;
	private void setInterval() {
		this.interval = RandomNumberGenerator.getInstance(lambda);
	}
	
	public GenerateCustomer() {
		this.type = "generating";
		this.id = generateCustomerID++;
		this.timer = new Timer();
	}
	
	@Override
	public synchronized void addObserver(Observer o) {
		// TODO Auto-generated method stub
		super.addObserver(o);
	}

	@Override
	public void notifyObservers() {
		// TODO Auto-generated method stub
		super.notifyObservers();
	}

	@Override
	protected synchronized void setChanged() {
		// TODO Auto-generated method stub
		super.setChanged();
	}
	
	private class GenerateTimeTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
