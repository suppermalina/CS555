/**
 * 
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.*;

/**
 *  The main purpose of this class is to collect system data peridicaly
 *  It implements the TimerTask, so it can be scheduled to collect data with a assigned time period
 *  takeServiceInformation() collects information brings by customers who already took service
 *  takeRejcetedInformation() records information for those customers rejected by the system.
 */ 
class InformationCollector extends TimerTask {
	private long[] systemContainer;
	private long[] serverContainer;
	private long[] queueContainer;
	private double[] rejectedContainer;
	private double[] servicedContainer;

	private int counterForCustomerServiced;
	private int counterForCustomerRejected;

	private double totalTime;

	private Deque<Information> queueInformationLink;
	private Deque<Information> serverInformationLink;
	private Deque<Information> systemInformationLink;
	private Deque<Information> rejectedQuantityInformationLink;
	private Deque<Information> servicedQuantityInformationLink;
	private Deque<Information> rejectedRatioInformationLink;
	private Deque<Information> servicedRatioInformationLink;
	private Deque<Information> finalStatistic;

	private Set<Task> customerRejected;
	private Set<Task> customerServiced;

	private Lock lock;
	private double samplingPeriod;
	private boolean trigger = false;
	private int endPoint = (int) (Controller.endingTime / Controller.period);

	InformationCollector() {
		this.totalTime = Controller.endingTime;
		this.lock = new ReentrantLock();

		this.systemContainer = new long[1];
		this.serverContainer = new long[1];
		this.queueContainer = new long[1];
		this.rejectedContainer = new double[1];
		this.servicedContainer = new double[1];

		this.counterForCustomerServiced = 0;
		this.counterForCustomerRejected = 0;

		this.systemInformationLink = new LinkedList<Information>();
		this.serverInformationLink = new LinkedList<Information>();
		this.queueInformationLink = new LinkedList<Information>();
		this.rejectedQuantityInformationLink = new LinkedList<Information>();
		this.servicedQuantityInformationLink = new LinkedList<Information>();
		this.rejectedRatioInformationLink = new LinkedList<Information>();
		this.servicedRatioInformationLink = new LinkedList<Information>();

		this.customerRejected = new HashSet<Task>();
		this.customerServiced = new HashSet<Task>();

		this.finalStatistic = new LinkedList<Information>();
	}


	protected void takeServiceInformation(Task servicedCustomer) {
		Customer informationProvider = (Customer) servicedCustomer;
		if (informationProvider.flag == true) {
			this.counterForCustomerServiced++;
			this.systemContainer[0] += informationProvider.timeInSystem();
			this.queueContainer[0] += informationProvider.timeInQueue();
			this.serverContainer[0] += informationProvider.timeInServer();
			this.customerServiced.add(informationProvider);
			this.servicedContainer[0] += this.counterForCustomerServiced;
		}
	}

	protected void takeRejcetedInformation(Task rejectedCustomer) {
		Customer informationProvider = (Customer) rejectedCustomer;
		if (informationProvider.flag == false) {
			this.counterForCustomerRejected++;
			System.out.println("REJECTED BY THE MODELSYSTEM");
			this.counterForCustomerRejected++;
			this.customerRejected.add(rejectedCustomer);
			this.rejectedContainer[0] += this.counterForCustomerRejected;
		}
	}

	protected Deque<Information> getSystemInform() {
		return this.systemInformationLink;
	}

	protected Deque<Information> getQueueInform() {
		return this.queueInformationLink;
	}

	protected Deque<Information> getServerInform() {
		return this.serverInformationLink;
	}

	protected Deque<Information> getRejectedQuantityInform() {
		return this.rejectedQuantityInformationLink;
	}

	protected Deque<Information> getRejectedRatioInform() {
		return this.rejectedRatioInformationLink;
	}

	protected Deque<Information> getServicedQuantityInform() {
		return this.servicedQuantityInformationLink;
	}

	protected Deque<Information> getServicedRatioInform() {
		return this.servicedRatioInformationLink;
	}

	protected Deque<Information> getFinalStatisticData() {
		double totalServiced = this.customerServiced.size();
		double totalRejected = this.customerRejected.size();
		this.finalStatistic.offerLast(new Information(totalServiced, 1.0));
		this.finalStatistic.offerLast(new Information(totalRejected, 2.0));
		return this.finalStatistic;
	}

	protected double totalQuantity() {
		return this.customerRejected.size() + this.customerServiced.size();
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
					double avgTimeInSystem = ((double) systemContainer[0] / 1000.0 );
					double avgTimeInQueue = ((double) queueContainer[0] / 1000.0);
					double avgTimeInServer = ((double) serverContainer[0] / 1000.0);
					double periodServiced = servicedContainer[0];
					double periodRejected = rejectedContainer[0];
					
					systemInformationLink.offerLast(new Information(avgTimeInSystem, timeStamp));
					queueInformationLink.offerLast(new Information(avgTimeInQueue, timeStamp));
					serverInformationLink.offerLast(new Information(avgTimeInServer, timeStamp));

					servicedQuantityInformationLink.offerLast(new Information(periodServiced, timeStamp));
					rejectedQuantityInformationLink.offerLast(new Information(periodRejected, timeStamp));

					systemContainer = new long[1];
					queueContainer = new long[1];
					serverContainer = new long[1];
					rejectedContainer = new double[1];
					servicedContainer = new double[1];
					counterForCustomerRejected = 0;
					counterForCustomerServiced = 0;
					trigger = false;
				} finally {
					lock.unlock();
				}
			}
		}
		System.out.println(systemInformationLink.peekLast().getAverageTime());
		System.out.println(queueInformationLink.peekLast().getAverageTime());
		System.out.println(serverInformationLink.peekLast().getAverageTime());
		System.out.println(servicedQuantityInformationLink.peekLast().getAverageTime());
		System.out.println(rejectedQuantityInformationLink.peekLast().getAverageTime());
		trigger = false;
	}

}
