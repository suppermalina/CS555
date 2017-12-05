/**
 * 
 */
package TestQuatz;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ExecutionalInstances.StatisticalClock;

/**
 * @author mali
 *
 */
public class TestJob implements Job {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(TestJob.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		if (logger.isDebugEnabled()) {
			logger.debug("execute(JobExecutionContext) - start"); //$NON-NLS-1$
		}

		// TODO Auto-generated method stub
		System.out.println("--------------------------------------------------------------------");
		System.out.println("Task start: " + StatisticalClock.CLOCK());
		JobDetail jobDetail = jobContext.getJobDetail();
		System.out.println("Task is: " + jobDetail.getJobDataMap().getString("Customer"));
		System.out.println("Task end: " + StatisticalClock.CLOCK() + ", key: " + jobDetail.getKey());
		//System.out.println("MyJob next scheduled time: " + jobContext.getNextFireTime());
		System.out.println("--------------------------------------------------------------------");
		setFlag();
		System.out.println("flag is " + TaskSchedule.flag);
		if (logger.isDebugEnabled()) {
			logger.debug("execute(JobExecutionContext) - end"); //$NON-NLS-1$
		}
	}
	
	public void setFlag() {
		TaskSchedule.flag = true;
	}

}
