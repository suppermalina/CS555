/**
 * 
 */
package ExecutionalInstances;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import org.rosuda.JRI.Rengine;

import ContainersInstance.Queueing;
import ContainersInstance.Server;
import ContainersInstance.StateList;
import ContainersInstance.StatisticalCounter;
import ContainersInstance.TaskList;
import EventsInstances.Customer;
import EventsInstances.GenerateCustomer;
import EventsInstances.PopCustomerOut;
import Model.BaseGenerator;
import Model.Containers;
import Model.Task;

/**
 * @author mali
 *
 */
public class Generator implements BaseGenerator {
	private final static Rengine ENGINE = new Rengine(new String[] {"--vanilla"}, false, null);
	/* (non-Javadoc)
	 * @see Model.BaseGenerator#getEvent(int, java.lang.String)
	 */
	@Override
	public Task getTask(String type) {
		// TODO Auto-generated method stub
		if (type.equalsIgnoreCase("customer")) {
			return new Customer();
		} else if (type.equalsIgnoreCase("generating")) {
			return new GenerateCustomer();
		} else if (type.equalsIgnoreCase("poping")) {
			return new PopCustomerOut();
		} else {
			System.exit(-1);
			new Exception("Illegal type!!!");
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see Model.BaseGenerator#getContainer(int, java.lang.String)
	 */
	@Override
	public Containers getContainer(String type) {
		// TODO Auto-generated method stub
		if (type.equalsIgnoreCase("server")) {
			return new Server();
		} else if (type.equalsIgnoreCase("queue")) {
			return new Queueing();
		} else if (type.equalsIgnoreCase("eventlist")) {
			return new TaskList();
		} else if (type.equalsIgnoreCase("statelist")) {
			return new StateList();
		} else if (type.equals("counter")) {
			return new StatisticalCounter();
		} else {
			System.exit(-1);
			new Exception("Illegal type!!!");
			return null;
		}
	}
	
}
