package Model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// At the very beginning, I planed to implement a service system contains two or more
// servers by combining two instanced Server together with a data structure
class Servers {	
	private static Server one;
	private static Server two;
	private static Lock lock;
	public static Server[] servers;
	private static Servers instance = null;;
	private static Queueing queue = Queueing.getInstance();
	protected static int customerInServers;
	Servers() {
		customerInServers = 0;
		servers = new Server[2];
		lock = new ReentrantLock();
		one = new Server(1);
		two = new Server(2);
		servers[0] = one;
		servers[1] = two;
	}
	
	protected static synchronized Servers getInstance() {
		if (instance == null) {
			instance = new Servers();
		}
		return instance;
	}
	
	protected static void takeIntoServer() {
		if (lock.tryLock()) {
			try {
				if (!queue.isIdle()) {
					customerInServers++;
					if (one.isIdle() && two.isIdle()) {
						Task temp = queue.popTaskOut();
						System.out.println("Servers are all idle " + " at: " + Controller.clock.CLOCK() + 
								" " + temp.toString() + " DIRECT!!!!!!!!");
						//Controller.reporter.serverLog("Servers are all idle " + " at: " + Controller.clock.CLOCK() + 
							//	" " + temp.toString() + " DIRECT!!!!!!!!");
						double random = Math.random();
						if (random <= 0.5) {
							one.takeTaskIn(temp);
						} else {
							two.takeTaskIn(temp);
						}
					} else if (one.isIdle()) {
						Task temp = queue.popTaskOut();
						//Controller.reporter.serverLog("Servers are all idle " + " at: " + Controller.clock.CLOCK() + 
							//	" " + temp.toString() + " DIRECT!!!!!!!!");
						System.out.println("Server one is idle " + " at: " + Controller.clock.CLOCK() + 
								" " + temp.toString() + " DIRECT!!!!!!!!");
						one.takeTaskIn(temp);
					} else if (two.isIdle()) {
						Task temp = queue.popTaskOut();
						//Controller.reporter.serverLog("Servers are all idle " + " at: " + Controller.clock.CLOCK() + 
							//	" " + temp.toString() + " DIRECT!!!!!!!!");
						//System.out.println("Server two is idle " + " at: " + Controller.clock.CLOCK() + 
							//	" " + temp.toString() + " DIRECT!!!!!!!!");
						two.takeTaskIn(temp);
					}
				}
			} finally {
				lock.unlock();
			}
		}

	}
	
	protected void cleanUnexecutedTasks() {
		if (servers[0].getSize() != 0) {
			Task temp = servers[0].server.remove(0);
			temp.cancel();
			temp = null;
		}
		if (servers[1].getSize() != 0) {
			Task temp = servers[1].server.remove(0);
			temp.cancel();
			temp = null;
		}
	}
	
	protected int getSize() {
		return one.getSize() + two.getSize();
	}
	
	// isIdle means all servers are empty
	protected synchronized boolean isIdle() {
		return one.isIdle() || two.isIdle();
	}
	
	// is !allFull means there's at least one server is idle
	protected boolean allFull() {
		return one.isFull() || two.isFull();
	}
	
}
