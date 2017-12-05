/**
 * 
 */
package ExecutionalInstances;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

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
	private Servers server;
	private Queueing queue;
	
	private Simulator() {
		org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");
		System.out.println("Simulator ready");

	}

	protected void setUp() {
		
		server = Servers.getInstance();
		queue = Queueing.getInstance();
		
	}

	private static Simulator instance = null;
	
	protected boolean specialSituation_SystemFull() {
		for (int i = 0; i < 2; i++) {
			Customer temp = (Customer) Generator.getTask("customer");
			System.out.println("Is the server null???" + (server.servers[i] == null));
			System.out.println("Is the customer null???" + (temp == null));

			server.servers[i].takeTaskIn(temp);
		}
		for (int i = 0; i < queue.capacity; i++) {
			Customer temp = (Customer) Generator.getTask("customer");
			queue.takeTaskIn(temp);
		}
		return true;
	}
	
	protected boolean specialSituation_PartialFull(int t) {
		if (t >= 2) {
			for (int i = 0; i < 2; i++) {
				server.servers[i].takeTaskIn(Generator.getTask("customer"));
			}
			modify();
		}
		for (int i = 0; i < t - 2; i++) {
			queue.takeTaskIn(Generator.getTask("customer"));
			Controller.signalFromController(false);
		}
		return true;
	}
	
	protected void modify() {
		server.servers[0].signalFromSimulator(false);
		server.servers[1].signalFromSimulator(false);
	}
	
	public static synchronized Simulator getInstance() {
		if (instance == null) {
			instance = new Simulator();
		}
		return instance;
	}

	public int currentState() {
		return queue.getSize() + server.getSize();
	}

	public void generateNewCustomer(Task t) {
		if (t != null) {
			Customer newCust = (Customer) Generator.getTask("customer");
			Controller.writeLog("Generating " + newCust.toString() + " at: " + StatisticalClock.CLOCK());
			queue.takeTaskIn(newCust);
		}
			
	}
		
}


