/**
 * 
 */
package Model;

/**
 * @author mali
 *
 */
public class SystemState {
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

}
