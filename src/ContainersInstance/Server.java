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
	private boolean allFull;
	private int custID;
	private long delay;
	private static Integer serverID = 1;
	private List<Task> server; 
	private double miu = 1;
	private Map<Integer, List<Task>> servers;
	private Set<Map.Entry<Integer, List<Task>>> set;
	private Deque<List<Task>> avaiable;
	private int targetServerID;
	
	private Map<Integer, Integer> locationOfCust;
	
	
	public Server(int numberOfServers) {
		this.avaiable = new LinkedList<List<Task>>();
		this.type = "SERVER";
		this.ID = serverID++;
		this.locationOfCust = new HashMap<Integer, Integer>();
		servers = new HashMap<Integer, List<Task>>();
		while (numberOfServers > 0) {
			server = new ArrayList<Task>();
			servers.put(numberOfServers--, server);
		}
		System.out.println("Server is ready");
	}
	
	public synchronized boolean allFull() {
		set = servers.entrySet();
		for (Map.Entry<Integer, List<Task>> entry : set) {
			if (entry.getValue().size() >= capacity) {
				allFull = true;
			} else {
				allFull = false;
				avaiable.offerLast(entry.getValue());
			}
		}
		return allFull;
	}
	
	public synchronized boolean isIdle() {
		set = servers.entrySet();
		for (Map.Entry<Integer, List<Task>> entry : set) {
			if (entry.getValue().size() == 0) {
				isIdle = true;
			} else {
				isIdle = false;
			}
		}
		return isIdle;
	}

	/* 
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public synchronized boolean takeTaskIn(Task e) {
		// TODO Auto-generated method stub
		Customer temp = (Customer)e;
		int custId = temp.getId();
		delay = (long) (RandomNumberGenerator.getInstance(miu) * 1000);
		Generator.intervalForPoping = delay;
		PopCustomerOut tempPop = (PopCustomerOut) Generator.getTask("poping");
		tempPop.markTargetID(temp.getId());
		Center.tasks.takeTaskIn(tempPop);
		if (isIdle) {
			double random = Math.random();
			if (random <= 0.5) {
				this.locationOfCust.put(custId, 1);
				return servers.get(1).add(e);
			} else {
				this.locationOfCust.put(custId, 2);
				return servers.get(2).add(e);
			}
		}
		return avaiable.pop().add(e);
	}
	
	public void findCustomer(int id) {
		this.targetServerID = this.locationOfCust.get(id);
	}
	
	/* 
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public synchronized Task popTaskOut() {
		// TODO Auto-generated method stub
		return servers.get(this.targetServerID).get(0);
	}
	
	public int firstCustID() {
		return server.get(0).getId();
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return servers.get(1).size() + servers.get(2).size();
	}
	
}
