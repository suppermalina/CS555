import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;

/**
 * 
 */

/**
 * @author mali
 *
 */
public class TestTimer {
	private Timer timer;
	private TimerTask task;
	private long delay = (long) (RandomNumberGenerator.getInstance(0.5) * 1000);
	
	private void makePlan() {
		timer = new Timer();
		task = new TestTimerTask();
		
		try {
			System.out.println("Delay is " + delay);
			System.out.println(StatisticalClock.CLOCK());
			timer.schedule(task, delay);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new TestTimer().makePlan();
	}
	
}
