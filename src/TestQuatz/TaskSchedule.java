package TestQuatz;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Date;

import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;

public class TaskSchedule {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = (Logger) LogManager.getLogger(TaskSchedule.class.getName());

	private static Scheduler tasklist = null;
	private static JobBuilder builder = null;
	// private static TriggerBuilder tirgger = null;
	private static long index = 1;
	private static JobDataMap data = null;
	public static boolean flag = false;

	public static void main(String[] args) throws SchedulerException {
		org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");
		
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}		
		long endingTime = 10000;
		
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
		try {
			tasklist = schedFact.getScheduler();
			tasklist.start();
			builder = JobBuilder.newJob(TestJob.class);
			// tirgger = TriggerBuilder.newTrigger();
			data = new JobDataMap();
		} catch (SchedulerException e) {
			logger.error("main(String[])", e); //$NON-NLS-1$

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long initialTime = StatisticalClock.CLOCK();
		while (StatisticalClock.CLOCK() <= endingTime) {
			if (initialTime == 0 || flag == true) {
				System.out.println("It's: " + StatisticalClock.CLOCK());
				System.out.println("The " + index + " flag is " + flag);
				long predictTime = (long) (RandomNumberGenerator.getInstance(0.5) * 1000);
				System.out.println("predictTime is: " + predictTime);
				String taskID = "taskID" + index;
				String Customer = "Customer"+index;
				String Trigger = "Trigger"+index++;
				JobDetail job = builder.newJob(TestJob.class).withIdentity(Customer, taskID).usingJobData(data)
						.build();
				Trigger trigger = TriggerBuilder.newTrigger().withIdentity(Trigger, taskID)
						.startAt(new Date(predictTime)).build();
				flag = false;
				tasklist.scheduleJob(job, trigger);
				initialTime = -1;
			}
			
		}

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}
}
