/**
 * 
 */
package EventsInstances;

import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import ExecutionalInstances.Controller;
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;
import ExecutionalInstances.TimerForLocal;
import Model.Observed;
import Model.Task;

/**
 * @author mali
 *
 */
public class GenerateCustomer extends Task implements Observed {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	// This is used to mark this object is the ith specific task being generated
	private static int generateCustomerID = 1;
	// idInTaskList is used to find this task object in task list. It will be
	// stored as the key value into a hash-map with the object as the value
	private int idInTaskList;

	// Supposed to have a controller, but not sure. May be it will be removed
	// later

	private Timer timer;
	public void setInterval(long interval) {
		// interval is the estimated interval between a new generated task and
		// the next
		// task which supposed to be generated
		this.interval = interval;

		// Tasks in the tasklist are sorted based on the terminalTime
		this.terminalTime = this.interval + this.initialTime;
	}

	public GenerateCustomer(long interval) {
		// A instanced controller here is a registered observer
		// Due to the controller class is a final class and the instance is
		// synchronized, so all signal tasks share the same instance
		
		this.initialTime = StatisticalClock.CLOCK();
		this.type = "generating";
		this.id = generateCustomerID++;
		this.idInTaskList = this.idForTaskList++;
		
		// The estimated length of a interval should be past to the task to
		// setup its terminaltime
		this.setInterval(interval);
		
		// A signal task should be generated with a timer
		// And the timer will require this signal task to inform the controller
		// when
		// the timer reaches the delay
		timer = new Timer();
		this.localTask = new TimerForLocal() {
			public void run() {
				notifyController();
			}
		};
		timer.schedule(localTask, interval);
	}

	@Override
	public boolean hasChanged() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private Controller controller;
	
	@Override
	public  void addController(Controller o) {
		// TODO Auto-generated method stub
		controller = o;
	}

	@Override
	public void notifyController() {
		// TODO Auto-generated method stub
		System.out.println("Ok, the " + testCounter++ + " times notify");
		controller.notified(this);
	}

	public int getIdInTaskList() {
		return this.idInTaskList;
	}

	public static int testCounter = 1;

}
