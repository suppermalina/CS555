/**
 * 
 */
package EventsInstances;

import java.util.Timer;
import java.util.TimerTask;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ContainersInstance.Servers;
import ExecutionalInstances.Controller;
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;
import Model.Observed;
import Model.Task;

/**
 * @author mali
 *
 */
public class PopCustomerOut extends Task implements Job {
	private int customerID;
	// This is used to mark this object is the ith specific task being generated
	private static int popCounter = 1;

	public PopCustomerOut() {
		// A instanced controller here is a registered observer
		// Due to the controller class is a final class and the instance is
		// synchronized, so all signal tasks share the same instance
		org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");
		this.type = "poping";
		this.id = popCounter++;
		this.initialTime = StatisticalClock.CLOCK();
		Controller.tasks.takeTaskIn(this);
	}

	public void setInterval(long interval) {
		this.interval = interval;
		this.terminalTime = this.initialTime + this.interval;
	}

	public void markTargetID(int id) {
		this.customerID = id;
	}

	public int getTargetID() {
		return this.customerID;
	}
		

	public String toString() {
		return this.getTyp() + this.id + " is generated at: " + this.initialTime + ", and it will be executed at "
				+ this.terminalTime;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		Controller.writeLog(this.toString() + " excuted at " + StatisticalClock.CLOCK());
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		int serverID = dataMap.getInt("ServerID");
		Servers.servers[serverID - 1].popTaskOut();
		// System.out.println(this.toString() + " excuted at " +
		// StatisticalClock.CLOCK());
	}

}
