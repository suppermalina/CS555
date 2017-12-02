/**
 * 
 */
package ExecutionalInstances;

import java.util.Observable;
import java.util.Observer;

import ContainersInstance.TaskList;
import EventsInstances.GenerateCustomer;
import EventsInstances.PopCustomerOut;
import Model.Task;

/**
 * @author mali
 *
 */
public final class Controller {
	private static Controller instance = null;
	private TaskList tasks = (TaskList) Generator.FACTORY.getContainer("tasklist");
	private Simulator simulator = new Simulator();
	
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
	}
	public synchronized void notified(Task t) {
		update(t);
	}
	
	private void update(Task t) {
		if (t instanceof GenerateCustomer) {
			simulator.generateNewCustomer(t);
		} else {
			simulator.popCustomer((PopCustomerOut)t);
		}
	}

}
