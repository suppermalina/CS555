/**
 * 
 */
package Model;

import java.util.Observable;
import java.util.TimerTask;

import Model.Event.Task;

/**
 * @author mali
 * Observed is the superclass of each specific events.
 * It contains common features in different evets.
 */
public abstract class Observed extends Observable {
	private int id;
	private String type;
	private long getState;
	private long initialTime;
	private long terminalTime;
	private Task timerTask;
	private Coordinator coordinator;
	private TimerTask task;
}
