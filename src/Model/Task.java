/**
 * 
 */
package Model;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import ExecutionalInstances.Coordinator;

/**
 * @author mali
 *
 */
public abstract class Task extends Observable implements Event {
	protected int id;
	protected String type;
	protected long initialTime;
	protected long terminalTime;
	protected double interval;

	/* (non-Javadoc)
	 * @see Model.Event#getId()
	 */
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	/* (non-Javadoc)
	 * @see Model.Event#getTyp()
	 */
	@Override
	public String getTyp() {
		// TODO Auto-generated method stub
		return type + " " + id;
	}

	@Override
	public long getInitialTime() {
		// TODO Auto-generated method stub
		return this.initialTime;
		
	}
	@Override
	public long getTerminalTime() {
		// TODO Auto-generated method stub
		return this.terminalTime;
	}
	public double getInterval() {
		// TODO Auto-generated method stub
		return interval;
	}

}
