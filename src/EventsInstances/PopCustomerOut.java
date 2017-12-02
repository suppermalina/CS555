/**
 * 
 */
package EventsInstances;

import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.Coordinator;
import ExecutionalInstances.RandomNumberGenerator;
import Model.Observed;
import Model.Task;

/**
 * @author mali
 *
 */
public class PopCustomerOut extends Task implements Observed {
	private static int popCounter = 1;
	private double miu;
	private Timer timer;
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
