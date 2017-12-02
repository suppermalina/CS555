/**
 * 
 */
package ExecutionalInstances;

import java.util.*;

import ContainersInstance.*;
import EventsInstances.Customer;
import EventsInstances.PopCustomerOut;
import Model.Containers;
import Model.Task;

/**
 * @author mali
 *
 */
public final class Simulator {
	private int numberOfServer = 2;
	private int numberOfQueue = 1;
	
	// This is for future development. I want to learn the situation that each server
	// has its own queue.
	// The container with the smallest size should always on the top
	private static PriorityQueue<Queueing> queues;
	private static PriorityQueue<Server> servers;
	
	// log records each action happens in the simulator
	
	private Comparator<Containers> myComparator;
	
	// If a system has multiple servers and multiple queues, and they are not combined
	// then we will need this to find the location of a specific customer
	private static Map<Integer, Containers> customerFinder;
	private static StatisticalCounter counter;
	
	// Only one controller is allowed
	private void setUp() {
		customerFinder = new HashMap<Integer, Containers>();
		queues = new PriorityQueue<Queueing>(10, new MyComparator());
		servers = new PriorityQueue<Server>(10, new MyComparator());
		while (numberOfServer > 0) {
			int counter = 1;
			servers.offer((Server)Generator.FACTORY.getContainer("server"));
			numberOfServer--;
		}
		while (numberOfQueue > 0) {
			int counter = 1;
			queues.offer((Queueing)Generator.FACTORY.getContainer("queue"));
			numberOfQueue--;
		}
		counter = (StatisticalCounter)Generator.FACTORY.getContainer("counter");
	}
	
	public static int currentState() {
		Set<Map.Entry<Integer, Containers>> containers = customerFinder.entrySet();
		int size = 0;
		for (Map.Entry<Integer, Containers> container : containers) {
			size += container.getValue().getSize();
		}
		return size;
			
	}
	
	public static void generateNewCustomer(Task t) {
		int index = 0;
		Customer newCust = null;
		Queueing tempQue = null;
		Server tempServer = null;
		if (t != null) {
			newCust = (Customer)Generator.FACTORY.getTask("customer");
			tempQue = queues.peek();
			tempServer = servers.peek();
		} else {
			System.exit(-1);
			System.out.println("The task for generating new customer is NULL!!!");
		}
		if (tempQue.isIdle()) {
			if (tempServer.isIdle()) {
				tempServer.takeTaskIn(newCust);
				newCust.setTimeEnteringServer();
				customerFinder.put(newCust.getId(), tempServer);
			} else {
				tempQue.takeTaskIn(newCust);
				customerFinder.put(newCust.getId(), tempQue);
			}
			newCust.setFlag();
			newCust = null;
		} else {
			if (!queues.peek().isFull()) {
				newCust.setFlag();
				tempQue.takeTaskIn(newCust);
				customerFinder.put(newCust.getId(), tempQue);
				newCust = null;
			} 
		}
		
		if (newCust != null) {
			counter.takeTaskIn(newCust);
		}
	}
	
	protected void popCustomer(PopCustomerOut p) {
		Queueing tempQue = null;
		Server tempServer = null;
		Customer targetCustomer = null;
		if (p != null) {
			Containers tempContainer = customerFinder.get(p.getTargetID());
			if (tempContainer instanceof Queueing) {
				tempQue = (Queueing)tempContainer;
				if (tempQue == null ||p.getTargetID() != tempQue.firstCustID()) {
					System.exit(-1);
					System.out.println("BUGS WITH POPPING FROM QUEUE. SELECTED QUEUE IS NULL OR ID DOES NOT MATCH");
				} else {
					tempServer = servers.peek();
					targetCustomer = (Customer) tempQue.popTaskOut();
					targetCustomer.setTimeEnteringServer();
					tempServer.takeTaskIn(targetCustomer);
				}
			} else {
				tempServer = (Server)tempContainer;
				if (tempServer == null ||p.getTargetID() != tempServer.firstCustID()) {
					System.exit(-1);
					System.out.println("BUGS WITH POPPING FROM SERVER. SELECTED SERVER IS NULL OR ID DOES NOT MATCH");
				} else {
					targetCustomer = (Customer)tempServer.popTaskOut();
					targetCustomer.setTerminalTime();
				}
			}
		}
	}
	
	protected class MyComparator implements Comparator<Containers> {

		@Override
		public int compare(Containers o1, Containers o2) {
			// TODO Auto-generated method stub
			int cOne = o1.getSize();
			int cTwo = o2.getSize();
			if (cOne == cTwo) {
				return 0;
			} else if (cOne < cTwo) {
				return -1;
			} else return 1;
		}
	}

}
