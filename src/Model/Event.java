/**
 * 
 */
package Model;

import java.util.*;

/**
 * @author mali
 * interface event defines those common actions could be done by an event.
 * Event is an abstract conception. In our project, a customer can be treated as
 * an event. The customer leaves the clinic or exists are all event.
 */
public interface Event {
	abstract int getId();
	abstract String getTyp();
	abstract void setInitialTime(long initialTime);
	abstract void setTerminalTime(long terminalTime);
	abstract long getInterval();
	class InitiateTimerTask extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}};
}
