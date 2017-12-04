/**
 * 
 */
package EventsInstances;

import java.util.Timer;
import java.util.TimerTask;

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
public class PopCustomerOut extends Task implements Observed {
	private int targetCustID;

	// This is used to mark this object is the ith specific task being generated
	private static int popCounter = 1;

	// idInTaskList is used to find this task object in task list. It will be
	// stored as the key value into a hash-map with the object as the value
	private int idInTaskList;

	// The service rate
	private double miu = 1;
	private Timer timer;
	// Supposed to have a controller, but not sure. May be it will be removed
	// later

	public PopCustomerOut(long interval) {
		// A instanced controller here is a registered observer
		// Due to the controller class is a final class and the instance is
		// synchronized, so all signal tasks share the same instance

		this.type = "poping";
		this.id = popCounter++;
		this.idInTaskList = this.idForTaskList++;
		this.initialTime = StatisticalClock.CLOCK();
		this.interval = interval;
		this.terminalTime = this.initialTime + this.interval;

		// The estimated length of a interval should be past to the task to
		// setup its terminaltime

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

	@Override
	public void addController(Controller o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyController() {
		System.out.println("pop notify");
		Controller.notified(this);
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

}
