package ContainersInstance;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import EventsInstances.Customer;
import EventsInstances.PopCustomerOut;
import ExecutionalInstances.Controller;
import ExecutionalInstances.Generator;
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;
import Model.Task;

// At the very beginning, I planed to implement a service system contains two or more
// servers by combining two instanced Server together with a data structure
public class Servers {	
	private static Server one;
	private static Server two;
	private static Queueing queue = null;
	private static Lock lock;

	protected static void takeQueue(Queueing queue) {
		Servers.queue = queue;
	}
	
	public Servers() {
		lock = new ReentrantLock();
		one = (Server) Generator.getContainer("server");
		two = (Server) Generator.getContainer("server");
	}
	
	protected static void takeIntoServer() {
		if (lock.tryLock()) {
			try {
				while (!queue.isIdle()) {
					if (one.isIdle() && two.isIdle()) {
						Task temp = queue.popTaskOut();
						Controller.writeLog("Servers are all idle " + " at: " + StatisticalClock.CLOCK() + 
								" " + temp.toString() + " DIRECT!!!!!!!!");
						double random = Math.random();
						if (random <= 0.5) {
							one.takeTaskIn(temp);
						} else {
							two.takeTaskIn(temp);
						}
					} else if (one.isIdle()) {
						Task temp = queue.popTaskOut();
						Controller.writeLog("Servers are all idle " + " at: " + StatisticalClock.CLOCK() + 
								" " + temp.toString() + " DIRECT!!!!!!!!");
						one.takeTaskIn(temp);
					} else if (two.isIdle()) {
						Task temp = queue.popTaskOut();
						Controller.writeLog("Servers are all idle " + " at: " + StatisticalClock.CLOCK() + 
								" " + temp.toString() + " DIRECT!!!!!!!!");
						two.takeTaskIn(temp);
					}
				}
			} finally {
				lock.unlock();
			}
		}

	}
	
	public synchronized int getSize() {
		return one.getSize() + two.getSize();
	}
	
	// isIdle means all servers are empty
	public synchronized static boolean isIdle() {
		return one.isIdle() && two.isIdle();
	}
	
	// is !allFull means there's at least one server is idle
	public synchronized boolean allFull() {
		return one.isFull() || two.isFull();
	}
	
}
