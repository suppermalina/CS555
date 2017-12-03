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


	// If a system has multiple servers and multiple queues, and they are not
	// combined
	// then we will need this to find the location of a specific customer
	private static Map<Integer, Containers> customerFinder;
	private static StatisticalCounter counter;
	private static Servers server;
	private static Queueing queue;

	private Simulator() {
		server = new Servers();
		queue = (Queueing) Generator.getContainer("queue");
		counter = (StatisticalCounter) Generator.getContainer("counter");
		System.out.println("Simulator ready");
	}

	private static final Simulator INSTANCE = new Simulator();

	public static Simulator getInstance() {
		return INSTANCE;
	}

	public synchronized static int currentState() {
		return queue.getSize() + server.getSize();
	}

	public synchronized static boolean generateNewCustomer(Task t) {
		Customer newCust = null;
		if (t == null) {
			// System.out.println("generateNewCustomer recieves null");
		} else {
			// System.out.println("generateNewCustomer received the task");
		}
		if (t != null) {
			newCust = (Customer) Generator.getTask("customer");
			sentToQueue(newCust);
			return true;
		} else {
			System.exit(-1);
			System.out.println("The task for generating new customer is NULL!!!");
		}
		return false;
	}

	private synchronized static void sentToQueue(Customer c) {
		Customer newCust = c;
		if (queue.isIdle()) {
			if (!server.allFull()) {
				newCust.setFlag();
				System.out.println("DIRECT SERVICE!!!");
				server.enterServers(newCust);
			} else {
				System.out.println("Waiting in the Queue, FIRST!!!");
				queue.takeTaskIn(newCust);
			}
			newCust = null;
		} else {
			System.out.println("queue is not idle");
			System.out.println(server.getSize());
			if (!queue.isFull()) {
				newCust.setFlag();
				System.out.println("Waiting in the Queue, SOMEWHERE!!!");
				queue.takeTaskIn(newCust);
				newCust = null;
			}
			System.out.println(queue.getSize());
			System.out.println("servers are all full!!!");
		}
		if (newCust != null) {
			counter.takeTaskIn(newCust);
			System.out.println("REJECTED!!!");
		}
	}

	protected synchronized void popCustomer(PopCustomerOut p) {
		if (p != null) {
			server.popCust(p.getPopedCustomerID());
		} else {
			System.exit(-1);
			System.out.println("How can a poping task be null");
		}
		if (!queue.isFull()) {
			server.enterServers(queue.popTaskOut());
		}
	}

}
