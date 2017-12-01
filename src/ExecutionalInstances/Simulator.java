/**
 * 
 */
package ExecutionalInstances;

import java.util.*;

import ContainersInstance.*;

/**
 * @author mali
 *
 */
public class Simulator {
	private TaskList tasks;
	private StateList states;
	private Generator factory;
	private int numberOfServer;
	private int numberOfQueue;
	
	// In our project they they are 1
	// Maybe in the future they will be changed into some larger numbers
	// for simulating a more complicated system
	private int numberOfTasktList;
	private int numberOfStateList;
	
	// This is for future development. I want to learn the situation that each server
	// has its own queue
	private Map<Integer, Queueing> queues;
	private Map<Integer, Server> servers;
	
	// Only one controller is allowed
	private final Coordinator controller = Coordinator.getInstance();
	
	private void initiateFactory() {
		factory = new Generator();
	}
	
	private void setUp() {
		while (numberOfServer > 0) {
			int counter = 1;
			servers.put(counter++, (Server)factory.getContainer("server"));
			numberOfServer--;
		}
		while (numberOfQueue > 0) {
			int counter = 1;
			queues.put(counter++, (Queueing)factory.getContainer("queue"));
		}
		tasks = (TaskList)factory.getContainer("tasklist");
		states = (StateList)factory.getContainer("statelist");
	}

}
