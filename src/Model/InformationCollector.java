/**
 * 
 */
package Model;

import java.util.*;
import java.util.concurrent.locks.*;

import EventsInstances.Customer;
import ExecutionalInstances.Controller;

/**
 * @author mali
 *
 */
public class InformationCollector extends TimerTask {
	private long[] systemContainer;
	private long[] serverContainer;
	private long[] queueContainer;
	private long[] rejectedContainer;
	private int counterForCustomerTookService;
	private int counterForCustomerRejected;
	private double totalTime;
	private Deque<Information> queueInformationLink;
	private Deque<Information> serverInformationLink;
	private Deque<Information> systemInformationLink;
	private Deque<Information> rejectedInformationLink;
	private Deque<Task> customerOutFromServer;
	private Set<Task> customerRejected;
	private Lock lock;
	private int caliber;
	private double samplingPeriod;
	private boolean trigger = false;
	private int endPoint = (int) (Controller.endingTime / Controller.period);

	public InformationCollector() {
		this.caliber = 0;
		this.totalTime = Controller.endingTime;
		this.lock = new ReentrantLock();
		this.systemContainer = new long[1];
		this.serverContainer = new long[1];
		this.queueContainer = new long[1];
		this.rejectedContainer = new long[1];
		this.counterForCustomerTookService = 0;
		this.counterForCustomerRejected = 0;
		this.systemInformationLink = new LinkedList<Information>();
		this.serverInformationLink = new LinkedList<Information>();
		this.queueInformationLink = new LinkedList<Information>();
		this.rejectedInformationLink = new LinkedList<Information>();
		this.customerOutFromServer = new LinkedList<Task>();
		this.customerRejected = new HashSet<Task>();
	}

	public void setPeriod(double period) {
		this.samplingPeriod = period;
	}

	public void takeServiceInformation(Task servicedCustomer) {
		this.counterForCustomerTookService++;
		Customer informationProvider = (Customer) servicedCustomer;
		this.systemContainer[0] += informationProvider.timeInSystem();
		this.queueContainer[0] += informationProvider.timeInQueue();
		this.serverContainer[0] += informationProvider.timeInServer();
	}

	public void takeRejcetedInformation(Task rejectedCustomer) {
		System.out.println("REJECTED BY THE MODELSYSTEM");
		this.counterForCustomerRejected++;
		//Customer rejected = (Customer) rejectedCustomer;
		//System.out.println(rejectedCustomer == null);
		
		this.customerRejected.add(rejectedCustomer);
		this.rejectedContainer[0] += this.customerRejected.size();
	}

	public Deque<Information> getSystemInform() {
		return this.systemInformationLink;
	}

	public Deque<Information> getQueueInform() {
		return this.queueInformationLink;
	}

	public Deque<Information> getServerInform() {
		return this.serverInformationLink;
	}

	public Deque<Information> getRejectedInform() {
		return this.rejectedInformationLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		trigger = true;
		System.out.println("before run()" + trigger);
		if (trigger) {
			if (lock.tryLock()) {
				try {
					double timeStamp = Controller.clock.CLOCK() / 1000.0;
					double avgTimeInSystem = ((double) systemContainer[0] / 1000.0);
					double avgTimeInQueue = ((double) queueContainer[0] / 1000.0);
					double avgTimeInServer = ((double) serverContainer[0] / 1000.0);
					double avgTimeBeRejected = ((double) rejectedContainer[0]);
					systemInformationLink.offerLast(new Information(avgTimeInSystem, timeStamp));
					queueInformationLink.offerLast(new Information(avgTimeInQueue, timeStamp));
					serverInformationLink.offerLast(new Information(avgTimeInServer, timeStamp));
					rejectedInformationLink.offer(new Information(avgTimeBeRejected, timeStamp));
					systemContainer = new long[1];
					queueContainer = new long[1];
					serverContainer = new long[1];
					rejectedContainer = new long[1];
					trigger = false;
				} finally {
					lock.unlock();
				}
			}
		}
		System.out.println("before run()" + trigger);
		System.out.println(systemInformationLink.peekLast().getAverageTime());
		System.out.println(queueInformationLink.peekLast().getAverageTime());
		System.out.println(serverInformationLink.peekLast().getAverageTime());
		System.out.println(rejectedInformationLink.peekLast().getAverageTime());

	}

}
