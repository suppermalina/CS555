/**
 * 
 */
package ExecutionalInstances;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import ContainersInstance.StateList;
import ContainersInstance.TaskList;
import EventsInstances.GenerateCustomer;
import EventsInstances.PopCustomerOut;
import Model.SystemState;
import Model.Task;

/**
 * @author mali
 *
 */
public final class Controller {
	// These two variables used be a part of GenerateCustomer and PopCustomerOut accordingly
	// but I think these two variables should be set and controlled by the controller
	// The average arriving rate
	private double lambda;
	// The service rate
	private double miu;
	private long globalTime = StatisticalClock.CLOCK();
	private long endingTime;
	private long delay;
	
	// These two lists were in the Simulator class
	public static StateList log = (StateList) Generator.FACTORY.getContainer("statelist");
	public static TaskList tasks = (TaskList) Generator.FACTORY.getContainer("tasklist");
	
	private static Controller instance = null;
	private Simulator simulator = new Simulator();
	
	// Timer should be setup by the controller not the task itself
	private Timer timer;
	private Map<Integer, Task> timerPool = new HashMap<Integer, Task>();
	public static Controller getInstance() {
        if (instance == null) {
            synchronized(Controller.class) {
                if (instance == null) {
                    instance = new Controller();
                }
            }
        }
        return instance;
    }
	
	public synchronized void addTask(Task t) {
		tasks.takeTaskIn(t);
		timerPool.put(t.getId(), t);
	}
	public synchronized void notified(Task t) {
		update(t);
	}
	 
	
	private void update(Task t) {
		log.takeTaskIn(t);
		if (t instanceof GenerateCustomer) {
			delay = (long) RandomNumberGenerator.getInstance(lambda);
			Generator.intervalForGenerating = delay;
			GenerateCustomer tempCust = (GenerateCustomer) Generator.FACTORY.getTask("generating");
			addTask(tempCust);
			simulator.generateNewCustomer(t);
		} else {
			delay = (long) RandomNumberGenerator.getInstance(miu);
			Generator.intervalForPoping = delay;
			PopCustomerOut tempPop = (PopCustomerOut) Generator.FACTORY.getTask("poping");
			addTask(tempPop);
			simulator.popCustomer((PopCustomerOut)t);
		}
		SystemState currentState = new SystemState(StatisticalClock.CLOCK(), Simulator.currentState());
		log.takeStateIn(currentState);
	}


}
