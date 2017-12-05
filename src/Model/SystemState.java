/**
 * 
 */
package Model;

/**
 * @author mali
 *
 */
public class SystemState extends Task {
	private int state;
	private long time;
	
	public SystemState(long time, int state) {
		this.time = time;
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
	public long getTime() {
		return time;
	}
	
	public String toString() {
		return "System has " + state + " customers at: " + time / 1000.0;
	}

}
