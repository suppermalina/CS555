/**
 * 
 */
package ContainersInstance;

import java.util.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import EventsInstances.Customer;
import EventsInstances.PopCustomerOut;
import ExecutionalInstances.Controller;
import ExecutionalInstances.Generator;
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.ReportGenerator;
import ExecutionalInstances.StatisticalClock;
import Model.Containers;
import Model.Task;
import TestQuatz.TaskSchedule;

/**
 * @author mali
 *
 */
public class Server extends Containers {
	private final int capacity = 1;
	private boolean isIdle;
	private boolean isFull;
	private int custID;
	private boolean cheating = true;
	// delay here is used to record the estimated service time
	private long delay;
	private static Integer serverID = 1;
	private List<Task> server;
	private double miu = 1;

	private static final Logger logger = (Logger) LogManager.getLogger(TaskSchedule.class.getName());

	public static Scheduler monitor = null;
	private static JobBuilder builder = null;
	// private static TriggerBuilder tirgger = null;
	private static long index = 1;
	private static JobDataMap data = null;
	

	public Server() {
		this.type = "SERVER";
		this.ID = this.serverID++;
		server = new ArrayList<Task>();
		org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");

		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
		try {
			monitor = schedFact.getScheduler();
			monitor.start();
			builder = JobBuilder.newJob(PopCustomerOut.class);
			data = new JobDataMap();
		} catch (SchedulerException e) {
			logger.error("main(String[])", e); //$NON-NLS-1$

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Server" + this.ID + "is ready");
	}

	public synchronized boolean isFull() {
		return getSize() > 0;
	}

	public boolean isIdle() {
		return getSize() == 0;
	}
	
	private boolean signal = true;
	
	public boolean signalFromSimulator(boolean stop) {
		signal = stop;
		return stop;
	}
	private boolean noMoreCheating() {
		return signalFromSimulator(signal);
	}

	/*
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public void takeTaskIn(Task e) {
		if (getSize() == 0) {
			// TODO Auto-generated method stub
			Customer temp = (Customer) e;
			String custInfor = temp.toString();
			Controller.writeLog(this.toString() + " takes " + e.toString() + " at: " + StatisticalClock.CLOCK());
			System.out.println(this.toString() + " takes " + e.toString() + " at: " + StatisticalClock.CLOCK());
			System.out.println(this.toString());
			temp.getServerTrigger(this);
			server.add(temp);
			// Once a customer was accepted by any one of the servers, then a
			// poping signal
			// task should be generated immediately
			long predictiTime = (long) (RandomNumberGenerator.getInstance(miu) * 1000);
			Controller.writeLog(this.toString() + " assigns " + e.toString() + " predictied service time: "
					+ StatisticalClock.CLOCK());
			JobDetail job = builder.newJob(PopCustomerOut.class).withIdentity(custInfor)
					.usingJobData("ServerID", this.ID).build();
			Controller.writeLog("scheduled job for " + custInfor + " was setted up at: " + StatisticalClock.CLOCK());
			long makeUpForCheating = 0;
			if (cheating) {
				makeUpForCheating = 250;
			} else {
				makeUpForCheating = 0;
			}
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(custInfor).startAt(new Date(predictiTime + makeUpForCheating))
					.build();

			// The task should be sent to the tasklist
			PopCustomerOut tempPop = new PopCustomerOut();
			tempPop.markTargetID(temp.getId());
			tempPop.setInterval(predictiTime);
			Controller.tasks.takeTaskIn(tempPop);
			try {
				if (cheating) {
					Controller.writeLog("******************************Earily bird is waiting in the server**********************************");
				}
				monitor.scheduleJob(job, trigger);
			} catch (SchedulerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/*
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public Task popTaskOut() {
		// TODO Auto-generated method stub
		Task t = server.remove(0);
		Controller.writeLog(
				this.toString() + " popps " + t.toString() + " predictied service time: " + StatisticalClock.CLOCK());
		Controller.counter.takeTaskIn(t);
		Servers.takeIntoServer();
		return t;
	}

	@Override
	public synchronized int getSize() {
		// TODO Auto-generated method stub
		return server.size();
	}

}
