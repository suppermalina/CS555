/**
 * 
 */
package EventsInstances;

import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.Coordinator;
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;
import Model.Observed;
import Model.Task;

/**
 * @author mali
 *
 */
public class GenerateCustomer extends Task implements Observed {
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
	
	
	private class GenerateTimeTask extends TimerTask {

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
	public void addCoordinator(Coordinator o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyCoordinatro(Task e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
