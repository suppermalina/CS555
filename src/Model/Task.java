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
	protected long getState;
	protected long initialTime;
	protected long terminalTime;
	protected TimerTask task;
	protected Timer timer;
	protected Task(String type, int id) {
		this.type = type;
		this.id = id;
		task = new InitiateTimerTask();
		timer = new Timer();
	}
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
		return type;
	}

	/* (non-Javadoc)
	 * @see Model.Event#setInitialTime(long)
	 */
	@Override
	public void setInitialTime(long initialTime) {
		// TODO Auto-generated method stub
		this.initialTime = initialTime;
	}

	/* (non-Javadoc)
	 * @see Model.Event#setTerminalTime(long)
	 */
	@Override
	public void setTerminalTime(long terminalTime) {
		// TODO Auto-generated method stub
		this.terminalTime = terminalTime;

	}
	
	protected long getInitialTime() {
		return this.initialTime;
	}
	
	protected long getTermialTime() {
		return this.terminalTime;
	}

	public long getInterval() {
		// TODO Auto-generated method stub
		return this.terminalTime - this.initialTime;
	}

}
