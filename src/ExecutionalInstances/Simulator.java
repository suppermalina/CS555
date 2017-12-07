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

	Simulator() {
		System.out.println("Simulator ready");
		lock = new ReentrantLock();
	}

	/*private ArrayList<Double> plotExistCDFX;
	private ArrayList<Integer> plotExistCDFY;
	private ArrayList<Double> plotServiceCDFX;
	private ArrayList<Integer> plotServiceCDFY;

	private ArrayList<Double> plotExistInQueX;
	private ArrayList<Integer> plotExistInQueY;
	private ArrayList<Double> plotInServiceX;
	private ArrayList<Integer> plotInServiceY;
	/*private ArrayList<Double> plotExistPMFX;
	private ArrayList<Integer> plotExistPMFY;
	private ArrayList<Double> plotServicePMFX;
	private ArrayList<Integer> plotServicePMFY;*/
	protected ArrayList<ArrayList<Double>> XAxis;
	protected ArrayList<ArrayList<Integer>> YAxis;
	
	protected ArrayList<Double> currentStateX;
	protected ArrayList<Double> currentStateY;
	protected ArrayList<ArrayList<Double>> pitcher;

	protected void setUp() {
		XAxis = new ArrayList<ArrayList<Double>>();
		YAxis = new ArrayList<ArrayList<Integer>>();
		/*plotExistCDFX = new ArrayList<Double>();
		plotExistCDFY = new ArrayList<Integer>();
		plotServiceCDFX = new ArrayList<Double>();
		plotServiceCDFY = new ArrayList<Integer>();

		plotExistInQueX = new ArrayList<Double>();
		plotExistInQueY = new ArrayList<Integer>();
		plotInServiceX = new ArrayList<Double>();
		plotInServiceY = new ArrayList<Integer>();*/
		
		currentStateX = new ArrayList<Double>();
		currentStateY = new ArrayList<Double>();
		
		/*plotExistPMFX = new ArrayList<Double>();
		plotExistPMFY = new ArrayList<Integer>();
		plotServicePMFX = new ArrayList<Double>();
		plotServicePMFY = new ArrayList<Integer>();*/

		/*plotExistCDFX.add(0, 0.0);
		plotExistInQueX.add(0, 1.0);
		plotServiceCDFX.add(0, 2.0);
		plotInServiceX.add(0, 3.0);

		plotExistCDFY.add(0, 0);
		plotExistInQueY.add(0, 1);
		plotServiceCDFY.add(0, 2);
		plotInServiceY.add(0, 3);*/

		/*XAxis.add(0, plotExistCDFX);
		XAxis.add(1, plotExistInQueX);
		XAxis.add(2, plotServiceCDFX);
		XAxis.add(3, plotInServiceX);*/
		//XAxis.add(1, plotExistPMFX);
		//XAxis.add(3, plotServicePMFX);
		/*YAxis.add(0, plotExistCDFY);
		YAxis.add(1, plotExistInQueY);
		YAxis.add(2, plotServiceCDFY);
		YAxis.add(3, plotInServiceY);*/
		server = new Servers();
		queue = new Queueing();
		totalCustomersBeingGenerated = 1;
		pitcher = new ArrayList<ArrayList<Double>>();
	}
	//int counter = 0;
	private int lastForExist = 0;
	private int lastForService = 0;
	protected void dataForPloting(long inputTime) {
		//boolean gate = false;
		//Controller.reporter.dataLog("1. InputTime: " + inputTime + ". Counter: " + counter + ". Gate is " + gate);
		//if (inputTime != 0) {
			//gate = true;
		//}
		//Controller.reporter.dataLog("2. InputTime: " + inputTime + ". Counter: " + counter + ". Gate is " + gate);
		//while (gate) {
			//gate = false;
			

			double time = inputTime / 1000000000.0;
			//Controller.reporter.dataLog(
				//	"3. Sampling time: " + inputTime + ". InputTime: " + inputTime + ". InputTime divided by 1000 is: "
					//		+ (inputTime / 1000.0) + ". Sampling times: " + ++counter + ". Gate is " + gate);
			XAxis.get(0).add(time);
			XAxis.get(1).add(time);
			XAxis.get(2).add(time);
			XAxis.get(3).add(time);
			YAxis.get(0).add(this.totalCustomersBeingGenerated);
			YAxis.get(1).add(queue.getSize());
			//YAxis.get(1).add(this.totalCustomersBeingGenerated - lastForExist);
			YAxis.get(2).add(server.customerInServers);
			YAxis.get(3).add(server.getSize());
			//YAxis.get(3).add(server.customerInServers - lastForService);
			lastForExist = this.totalCustomersBeingGenerated;
			lastForService = server.customerInServers;
			

			//counter = 0;
			
			// Controller.reporter.dataLog("6. InputTime: " + inputTime + ". Counter: " + ++counter + ". Gate is " + gate);
		//}

	}
	
	

	protected Map<Set<Customer>, Integer> rejectStatData() {
		Set<Customer> set = queue.rejected;
		Map<Set<Customer>, Integer> map = new HashMap<Set<Customer>, Integer>();
		map.put(set, totalCustomersBeingGenerated);
		return map;
	}
	
	// This is for plot the average number of customers in a system at each observation
	protected void plotState(long inputTime) {
		double time = inputTime / 1000000000.0;
		System.out.println(time);
		this.currentStateX.add(time);
		System.out.println(this.currentState());
		this.currentStateY.add((double)(this.currentState()));
	}
	
	protected ArrayList<ArrayList<Double>> sendData() {
		pitcher.add(currentStateX);
		pitcher.add(currentStateY);
		if(pitcher == null) {
			System.out.println("PITCHER IS DAMN NULL");
			System.exit(-1);
		}
		return pitcher;
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
			Controller.reporter.generatingLog(newCust.toString() + " was generated at: " + Controller.clock.CLOCK()
					+ ", involked by " + t.getTyp() + t.getId());
			queue.takeTaskIn(newCust);
		}

	}

}
