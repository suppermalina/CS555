/**
 * 
 */
package EventsInstances;

import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.RandomNumberGenerator;
import Model.Task;

/**
 * @author mali
 *
 */
public class PopCustomerOut extends Task {
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

}
