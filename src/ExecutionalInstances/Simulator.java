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
public class Simulator {
	private int numberOfServer;
	private int numberOfQueue;
	
	// In our project they they are 1
	// Maybe in the future they will be changed into some larger numbers
	// for simulating a more complicated system
	private int numberOfTasktList;
	private int numberOfStateList;
	
	// This is for future development. I want to learn the situation that each server
	// has its own queue
	private PriorityQueue<Queueing> queues;
	private PriorityQueue<Server> servers;
	private StateList log;
	private Comparator<Containers> myComparator;
	private Map<Integer, Containers> customerFinder;
	
	// Only one controller is allowed
	private void setUp() {
		StateList log = (StateList) Generator.FACTORY.getContainer("statelist");
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
		}
	}
	
	protected void generateNewCustomer(Task t) {
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
				newCust.setTimeInServer();
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
			log.takeTaskIn(newCust);
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
					targetCustomer.setTimeInServer();
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
					log.takeTaskIn(targetCustomer);
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
