/**
 * 
 */
package Model;

import java.util.Set;
import java.util.TimerTask;

import ExecutionalInstances.Center;

/**
 * @author mali
 *
 */
public interface Observed {
	abstract boolean hasChanged();
	abstract void addController(Center o);
	abstract void notifyController();
}
