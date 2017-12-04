/**
 * 
 */
package Model;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author mali
 *
 */
public abstract class Task {
	// This variable is set for those tasks will be send to the tasklist
	protected static int idForTaskList = 1;
	protected int id;
	protected String type;
	protected long initialTime;
	protected long terminalTime;
	protected long interval;
	protected TimerTask localTask;
	
	public int getId() {
		return id;
	}
	public TimerTask getLocalTask() {
		return localTask;
	}
	public String getTyp() {
		return type;
	}

	public long getInitialTime() {
		return this.initialTime;
	}
	public long getTerminalTime() {
		return this.terminalTime;
	}
	public long getInterval() {
		return interval;
	}
	
	public String getTimeInform() {
		return this.toString() + " initialtime is: " + this.initialTime + ", interval is: " + this.interval + ", terminaltime is: " + this.terminalTime;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj instanceof Task) {
			Task o = (Task)obj;
			if (o.type == this.type && o.id == this.id) {
				return true;
			} else return false;
		}
		System.out.println("The task which is going to be popped does not match the information from the controller");
		System.exit(-1);
		return false;
	}

	public String toString() {
		return this.type + " " + this.id;
	}

}
