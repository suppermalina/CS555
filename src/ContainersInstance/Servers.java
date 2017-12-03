package ContainersInstance;

import EventsInstances.Customer;
import EventsInstances.PopCustomerOut;
import ExecutionalInstances.Center;
import ExecutionalInstances.Generator;
import ExecutionalInstances.RandomNumberGenerator;
import Model.Task;

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
	
	public int getSize() {
		return one.getSize() + two.getSize();
	}
	
	public boolean isIdle() {
		return one.isIdle() && two.isIdle();
	}
	
	public boolean allFull() {
		return one.isFull() || two.isFull();
	}
	
	public Task popCust(int id) {
		if (one.compare(id)) {
			return one.popTaskOut();
		} else if (two.compare(id)) {
			return two.popTaskOut();
		} else return null;
	}
}
