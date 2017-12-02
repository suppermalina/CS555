/**
 * 
 */
package Model;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.Controller;

/**
 * @author mali
 *
 */
public abstract class Task extends TimerTask {
	protected int id;
	protected String type;
	protected long initialTime;
	protected long terminalTime;
	protected double interval;

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
