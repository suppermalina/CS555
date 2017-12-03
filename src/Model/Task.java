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

	public int getId() {
		return id;
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
	public double getInterval() {
		return interval;
	}
	
	public String toString() {
		return this.type + " " + this.id;
	}

}
