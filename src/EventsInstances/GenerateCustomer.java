/**
 * 
 */
package EventsInstances;

import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.Controller;
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
	private Controller controller;
	private void setInterval() {
		this.interval = RandomNumberGenerator.getInstance(lambda);
	}
	
	public GenerateCustomer() {
		this.type = "generating";
		this.id = generateCustomerID++;
		this.timer = new Timer();
	}

	@Override
	public boolean hasChanged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addController(Controller o) {
		// TODO Auto-generated method stub
		controller = Controller.getInstance();
	}

	@Override
	public void notifyController(Task e) {
		// TODO Auto-generated method stub
		controller.notified(e);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
