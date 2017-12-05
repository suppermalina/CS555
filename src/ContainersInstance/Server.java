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
		this.ID = serverID++;
		server = new ArrayList<Task>();
		org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");

		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
		try {
			monitor = schedFact.getScheduler();
			monitor.start();
			builder = JobBuilder.newJob(Customer.class);
			data = new JobDataMap();
		} catch (SchedulerException e) {
			logger.error("main(String[])", e); //$NON-NLS-1$

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Server is ready");
	}
	
	public synchronized boolean isFull() {
		return server.size() > 0;
	}
	
	public synchronized boolean isIdle() {
		return server.size() == 0;
	}
	
	/* 
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	@Override
	public void takeTaskIn(Task e) {
		if (server.size() == 0) {
			// TODO Auto-generated method stub
			Customer temp = (Customer)e;
			Controller.writeLog(this.toString() + " takes " + e.toString() + " at: " + StatisticalClock.CLOCK());
			
			// Once a customer was accepted by any one of the servers, then a poping signal
			// task should be generated immediately
			long predictiTime = (long) (RandomNumberGenerator.getInstance(miu) * 1000);
			Controller.writeLog(this.toString() + " assigns " + e.toString() + " predictied service time: " + StatisticalClock.CLOCK());

			JobDetail job = builder.newJob(Customer.class).withIdentity(temp.toString()).usingJobData(data).storeDurably(false)
					.build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("Trigger for " + temp.toString())
					.startAt(new Date(predictiTime)).build();
			// The task should be sent to the tasklist
			PopCustomerOut tempPop = new PopCustomerOut();
			tempPop.markTargetID(temp.getId());
			tempPop.setInterval(predictiTime);
			Controller.tasks.takeTaskIn(tempPop);
			try {
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
		
		Servers.takeIntoServer();
		return t;
	}
	
	@Override
	public synchronized int getSize() {
		// TODO Auto-generated method stub
		return server.size();
	}
	
}
