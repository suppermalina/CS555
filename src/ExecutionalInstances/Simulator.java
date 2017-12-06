/**
 * 
 */
package ExecutionalInstances;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
	protected Servers server;
	private Queueing queue;
	public int totalCustomersBeingGenerated;
	private Lock lock;
	private Simulator() {
		System.out.println("Simulator ready");
		lock = new ReentrantLock();
	}
	
	private ArrayList<Long> plotExistCDFX;
	private ArrayList<Integer> plotExistCDFY;
	private ArrayList<Long> plotServiceCDFX;
	private ArrayList<Integer> plotServiceCDFY;

	private ArrayList<Long> plotExistPMFX;
	private ArrayList<Integer> plotExistPMFY;
	private ArrayList<Long> plotServicePMFX;
	private ArrayList<Integer> plotServicePMFY;
	protected ArrayList<ArrayList<Long>> XAxis;
	protected ArrayList<ArrayList<Integer>> YAxis;
	
	protected void setUp() {
		XAxis = new ArrayList<ArrayList<Long>>();
		YAxis = new ArrayList<ArrayList<Integer>>();
		plotExistCDFX = new ArrayList<Long>();
		plotExistCDFY = new ArrayList<Integer>();
		plotServiceCDFX = new ArrayList<Long>();
		plotServiceCDFY = new ArrayList<Integer>();

		plotExistPMFX = new ArrayList<Long>();
		plotExistPMFY = new ArrayList<Integer>();
		plotServicePMFX = new ArrayList<Long>();
		plotServicePMFY = new ArrayList<Integer>();
		
		XAxis.add(0, plotExistCDFX);
		XAxis.add(1, plotExistPMFX);
		XAxis.add(2, plotServiceCDFX);
		XAxis.add(3, plotServicePMFX);
		
		YAxis.add(0, plotExistCDFY);
		YAxis.add(1, plotExistPMFY);
		YAxis.add(2, plotServiceCDFY);
		YAxis.add(3, plotServicePMFY);
		server = Servers.getInstance();
		queue = Queueing.getInstance();
		totalCustomersBeingGenerated = 1;
	}
	private int lastForExist;
	private int lastForService;
	protected void dataForPloting(long time) {
		if(lock.tryLock()) {
			try {
				
				XAxis.get(0).add(time);
				XAxis.get(1).add(time);
				XAxis.get(2).add(time);
				XAxis.get(3).add(time);
				YAxis.get(0).add(this.totalCustomersBeingGenerated);
				YAxis.get(2).add(server.customerInServers);
				if(time <= Controller.samplePoint) {
					YAxis.get(1).add(this.totalCustomersBeingGenerated);
					YAxis.get(3).add(server.customerInServers);	
					this.lastForExist = this.totalCustomersBeingGenerated;
					this.lastForService = server.customerInServers;
				} else {
					YAxis.get(1).add(this.totalCustomersBeingGenerated - this.lastForExist);
					YAxis.get(3).add(server.customerInServers - this.lastForService);	
					this.lastForExist = this.totalCustomersBeingGenerated - this.lastForExist;
					this.lastForService = server.customerInServers - this.lastForService;
				} 
			} finally {
				lock.unlock();
			}
		}
		
	}

	
	
	protected Map<Set<Customer>, Integer> rejectStatData() {
		Set<Customer> set = queue.rejected;
		Map<Set<Customer>, Integer> map = new HashMap<Set<Customer>, Integer>();
		map.put(set, totalCustomersBeingGenerated);
		return map;
	}

	private static Simulator instance = null;
	
	protected boolean specialSituation_SystemFull() {
		for (int i = 0; i < 2; i++) {
			server.servers[i].takeTaskIn(Generator.getTask("customer"));
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
		}
		for (int i = 0; i < t - 2; i++) {
			queue.takeTaskIn(Generator.getTask("customer"));
		}
		return true;
	}
	
	protected void modify() {
		server.servers[0].modify();
		server.servers[1].modify();
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

	public synchronized void generateNewCustomer(Task t) {
		if (t != null) {
			totalCustomersBeingGenerated++;
			Customer newCust = (Customer) Generator.getTask("customer");
			Controller.reporter.generatingLog(newCust.toString() + " was generated at: " + StatisticalClock.CLOCK() 
					+ ", involked by " + t.getTyp() + t.getId());
			queue.takeTaskIn(newCust);
		}
			
	}
		
}


