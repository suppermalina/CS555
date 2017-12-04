/**
 * 
 */
package ExecutionalInstances;

import java.util.*;
import java.util.concurrent.locks.*;

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

	// If a system has multiple servers and multiple queues, and they are not
	// combined
	// then we will need this to find the location of a specific customer
	private Map<Integer, Containers> customerFinder;
	private StatisticalCounter counter;
	private Servers server;
	private Queueing queue;
	private Lock lock;

	private Simulator() {
		System.out.println("Simulator ready");
	}

	protected void setUp() {
		server = new Servers();
		queue = (Queueing) Generator.getContainer("queue");
		counter = (StatisticalCounter) Generator.getContainer("counter");
		lock = new ReentrantLock();
	}

	private static Simulator instance = null;

	public static synchronized Simulator getInstance() {
		if (instance == null) {
			instance = new Simulator();
		}
		return instance;
	}

	public synchronized int currentState() {
		return queue.getSize() + server.getSize();
	}

	public void generateNewCustomer(Task t) {
		if (lock.tryLock()) {
			Controller.writeLog(
					t.toString() + " is holding the lock in " + "generateNewCustomer at: " + StatisticalClock.CLOCK());
			try {
				Customer newCust = null;
				int massage = 0;
				if (t != null && Controller.tasks.popTaskOut(t)) {
					newCust = (Customer) Generator.getTask("customer");
					// Once a new customer has been generated, the receiver
					// should be
					// informed
					// Then a new generating signal task should be scheduled
					massage = 1;
					Controller.listener(massage);
					Controller.writeLog("Massage is changed to true " + " at: " + StatisticalClock.CLOCK());
					Controller.writeLog("Generating " + newCust.toString() + " at: " + StatisticalClock.CLOCK());
					// This part used to be a method called toqueue()
					if (queue.isIdle()) {
						Controller.writeLog("Queue takes " + newCust.toString() + " at: " + StatisticalClock.CLOCK());
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
							Controller.writeLog(
									"Wating int the queue " + newCust.toString() + " at: " + StatisticalClock.CLOCK());
							newCust.setFlag();
							System.out.println("Waiting in the Queue, SOMEWHERE!!!");
							queue.takeTaskIn(newCust);
							newCust = null;
						} else if (queue.isFull() && !server.allFull()) {
							Server tempServer = server.getTheFreeOne();
							tempServer.takeTaskIn(queue.popTaskOut());
							queue.takeTaskIn(newCust);
							newCust = null;
						}
					}
					if (newCust != null) {
						counter.takeTaskIn(newCust);
						Controller.writeLog(newCust.toString() + " is rejected at: " + StatisticalClock.CLOCK());
						System.out.println("REJECTED!!!");
					}
				} else {
					System.out.println("Generate signal does not match the one on the top of tasklist");
					//System.exit(-1);
				}
			} finally {
				Controller.writeLog(
						t.toString() + " is going to unlock " + "generateNewCustomer at: " + StatisticalClock.CLOCK());
				lock.unlock();
			}
		} else {
			Controller.writeLog(t.toString() + " is waiting for the lock in " + "generateNewCustomer at: "
					+ StatisticalClock.CLOCK());
		}
	}

	protected void popCustomer(PopCustomerOut p) {
		if (lock.tryLock()) {
			Controller.writeLog(
					p.toString() + " is holding the lock in " + "popCustomer at: " + StatisticalClock.CLOCK());
			try {
				if (p != null && Controller.tasks.popTaskOut(p)) {
					System.out.println("pop requrest received");
					Customer poped = (Customer) server.popCust(p.getTargetID());
					Controller.writeLog("Pop " + poped.toString() + " at: " + StatisticalClock.CLOCK());
					// This if is for testing
					if (poped != null) {
						System.out.println(poped.getTyp() + " " + poped.getId() + " service done");
					}

					// This if...else... branch is for testing
					if (!queue.isIdle()) {
						System.out.println("queue has " + queue.getSize() + " customers");
					} else {
						System.out.println("queue is empty");
					}

					if (!queue.isIdle()) {
						System.out.println("queue is not idle");
						server.enterServers(queue.popTaskOut());
					}
				} else {
					System.out.println("Pop signal does not match the one on the top of tasklist");
					//System.exit(-1);
				}
			} finally {
				Controller.writeLog(
						p.toString() + " is going to unlock " + "popCustomer at: " + StatisticalClock.CLOCK());
				lock.unlock();
			}
		} else {
			Controller.writeLog(
					p.toString() + " is waiting for the lock in " + "popCustomer at: " + StatisticalClock.CLOCK());
		}
	}

}
