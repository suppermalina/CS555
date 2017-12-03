import java.util.TimerTask;

import ExecutionalInstances.StatisticalClock;

/**
 * 
 */

/**
 * @author mali
 *
 */
public class TestTimerTask extends TimerTask {

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("It's your term!");
		System.out.println(StatisticalClock.CLOCK() / 1000.0);
	}

}
