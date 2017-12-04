package ContainersInstance;

import EventsInstances.Customer;
import EventsInstances.PopCustomerOut;
import ExecutionalInstances.Controller;
import ExecutionalInstances.Generator;
import ExecutionalInstances.RandomNumberGenerator;
import Model.Task;

// At the very beginning, I planed to implement a service system contains two or more
// servers by combining two instanced Server together with a data structure
public class Servers {	
	Server one;
	Server two;
	
	public Servers() {
		one = (Server) Generator.getContainer("server");
		two = (Server) Generator.getContainer("server");
	}
	
	public synchronized boolean enterServers(Task e) {
		if (isIdle()) {
			double random = Math.random();
			if (random <= 0.5) {
				one.takeTaskIn(e);
			} else {
				two.takeTaskIn(e);
			}
		} else if (!one.isFull()) {
			one.takeTaskIn(e);
		} else {
			two.takeTaskIn(e);
		}
		return true;
	}
	
	public synchronized Server getTheFreeOne() {
		if (one.isIdle()) {
			return one;
		} else if (two.isIdle()) {
			return two;
		} return null;
	}
	
	public synchronized int getSize() {
		return one.getSize() + two.getSize();
	}
	
	// isIdle means all servers are empty
	public synchronized boolean isIdle() {
		return one.isIdle() && two.isIdle();
	}
	
	// is !allFull means there's at least one server is idle
	public synchronized boolean allFull() {
		return one.isFull() || two.isFull();
	}
	
	public synchronized Task popCust(int id) {
		if (one.findCustomer(id) == true) {
			return one.popTaskOut();
		} else if (two.findCustomer(id) == true) {
			return two.popTaskOut();
		} else return null;
	}
}
