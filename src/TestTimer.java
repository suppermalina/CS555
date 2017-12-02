import java.util.Timer;
import java.util.TimerTask;

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
	private long delay = 5000;
	
	private void makePlan() {
		timer = new Timer();
		task = new TestTimerTask();
		
		try {
			System.out.println(System.currentTimeMillis());
			timer.schedule(task, delay );
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new TestTimer().makePlan();
	}
	
}
