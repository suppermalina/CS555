/**
 * 
 */
package ContainersInstance;

import java.util.*;

import EventsInstances.Customer;
import EventsInstances.PopCustomerOut;
import ExecutionalInstances.Controller;
import ExecutionalInstances.Generator;
import ExecutionalInstances.RandomNumberGenerator;
import Model.Containers;
import Model.Task;

/**
 * @author mali
 *
 */
public class Server extends Containers {
	private int custID;
	private long delay;
	private static Integer serverID = 1;
	private List<Task> server = new ArrayList<Task>(container);
	private double miu;
	
	public Server() {
		this.type = "SERVER";
		this.ID = serverID++;
	}

	public synchronized boolean isFull() {
		return container.size() >= 1;
	}
	
	public synchronized boolean isIdle() {
		return container.isEmpty();
	}

	/* 
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public synchronized boolean takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		Customer temp = (Customer)e;
		delay = (long) RandomNumberGenerator.getInstance(miu);
		Generator.intervalForPoping = delay;
		PopCustomerOut tempPop = (PopCustomerOut) Generator.FACTORY.getTask("poping");
		tempPop.markTargetID(temp.getId());
		Controller.tasks.takeTaskIn(tempPop);
		return server.add(e);
		
	}

	/* 
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return server.get(0);
	}
	
	public int firstCustID() {
		return server.get(0).getId();
	}
	
}
