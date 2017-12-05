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
	private static Lock lock;
	public static Server[] servers;
	private static Servers instance = null;;
	private static Queueing queue = Queueing.getInstance();
	public Servers() {
		servers = new Server[2];
		lock = new ReentrantLock();
		one = (Server) Generator.getContainer("server");
		two = (Server) Generator.getContainer("server");
		servers[0] = one;
		servers[1] = two;
	}
	
	public static synchronized Servers getInstance() {
		if (instance == null) {
			instance = new Servers();
		}
		return instance;
	}
	
	protected static void takeIntoServer() {
		if (lock.tryLock()) {
			try {
				if (!queue.isIdle()) {
					if (one.isIdle() && two.isIdle()) {
						Task temp = queue.popTaskOut();
						System.out.println("Servers are all idle " + " at: " + StatisticalClock.CLOCK() + 
								" " + temp.toString() + " DIRECT!!!!!!!!");
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
						System.out.println("Server one is idle " + " at: " + StatisticalClock.CLOCK() + 
								" " + temp.toString() + " DIRECT!!!!!!!!");
						one.takeTaskIn(temp);
					} else if (two.isIdle()) {
						Task temp = queue.popTaskOut();
						Controller.writeLog("Servers are all idle " + " at: " + StatisticalClock.CLOCK() + 
								" " + temp.toString() + " DIRECT!!!!!!!!");
						System.out.println("Server two is idle " + " at: " + StatisticalClock.CLOCK() + 
								" " + temp.toString() + " DIRECT!!!!!!!!");
						two.takeTaskIn(temp);
					}
				}
			} finally {
				lock.unlock();
			}
		}

	}
	
	public int getSize() {
		return one.getSize() + two.getSize();
	}
	
	// isIdle means all servers are empty
	public synchronized boolean isIdle() {
		return one.isIdle() || two.isIdle();
	}
	
	// is !allFull means there's at least one server is idle
	public boolean allFull() {
		return one.isFull() || two.isFull();
	}
	
}
