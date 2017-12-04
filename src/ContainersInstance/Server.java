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
import ExecutionalInstances.StatisticalClock;
import Model.Containers;
import Model.Task;

/**
 * @author mali
 *
 */
public class Server extends Containers {
	private final int capacity = 1;
	private boolean isIdle;
	private boolean isFull;
	private int custID;
	
	// delay here is used to record the estimated service time
	private long delay;
	private static Integer serverID = 1;
	private List<Task> server; 
	private double miu = 1;
	
	private Map<Integer, Integer> locationOfCust;
	
	
	public Server(int numberOfServers) {
		this.type = "SERVER";
		this.ID = serverID++;
		this.locationOfCust = new HashMap<Integer, Integer>();
		server = new ArrayList<Task>();
		System.out.println("Server is ready");
	}
	
	public synchronized boolean isFull() {
		return server.size() > 0;
	}
	
	public synchronized boolean isIdle() {
		return server.size() == 0;
	}

	/* 
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public synchronized boolean takeTaskIn(Task e) {
		if (server.size() == 0) {
			// TODO Auto-generated method stub
			Customer temp = (Customer)e;
			Controller.writeLog(this.toString() + " takes " + e.toString() + " at: " + StatisticalClock.CLOCK());
			
			// Once a customer was accepted by any one of the servers, then a poping signal
			// task should be generated immediately
			delay = (long) (RandomNumberGenerator.getInstance(miu) * 1000);
			Generator.intervalForPoping = delay;
			PopCustomerOut tempPop = (PopCustomerOut) Generator.getTask("poping");
			tempPop.markTargetID(temp.getId());
			
			Controller.writeLog(tempPop.toString() + " is generated for " + e.toString() + " at: " + StatisticalClock.CLOCK());
			Controller.writeLog(tempPop.toString() + " interval time is: " + tempPop.getTimeInform());
			// The task should be sent to the tasklist
			Controller.tasks.takeTaskIn(tempPop);
			return server.add(e);
		} 
		return false;
	}
	
	/* 
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return server.get(0);
	}
	
	public synchronized boolean findCustomer(int id) {
		if (isFull()) {
			return server.get(0).getId() == id;
		} else {
			return false;
		}
	}

	@Override
	public synchronized int getSize() {
		// TODO Auto-generated method stub
		return server.size();
	}
	
}
