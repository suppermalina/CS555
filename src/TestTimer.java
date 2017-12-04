import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;
import ExecutionalInstances.TestSingleton;

/**
 * 
 */

/**
 * @author mali
 *
 */
public class TestTimer implements Runnable{
	
	private void makePlan() {
	}
	
	@Override
	public synchronized void run() {
		// TODO Auto-generated method stub
		long waitingTime = (long) (RandomNumberGenerator.getInstance(0.5) * 1000);
		try {
			long starttime = StatisticalClock.CLOCK();
			System.out.println("Waiting starts sat: " + starttime);
			System.out.println("Waiting: " + waitingTime + " ending time suppose to be " + (starttime + waitingTime));
			this.wait(waitingTime);
			System.out.println("Waiting end at: " + StatisticalClock.CLOCK());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
