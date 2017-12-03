/**
 * 
 */
package ContainersInstance;

import java.util.*;

import EventsInstances.Customer;
import EventsInstances.PopCustomerOut;
import ExecutionalInstances.Center;
import ExecutionalInstances.Generator;
import ExecutionalInstances.RandomNumberGenerator;
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
		// TODO Auto-generated method stub
		Customer temp = (Customer)e;
		delay = (long) (RandomNumberGenerator.getInstance(miu) * 1000);
		Generator.intervalForPoping = delay;
		PopCustomerOut tempPop = (PopCustomerOut) Generator.getTask("poping");
		tempPop.markTargetID(temp.getId());
		Center.tasks.takeTaskIn(tempPop);
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
	
	public synchronized boolean compare(int id) {
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
