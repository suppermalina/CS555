/**
 * 
 */
package Model;

import java.util.Set;
import java.util.TimerTask;

import ExecutionalInstances.Controller;

/**
 * @author mali
 *
 */
public interface Observed {
	abstract boolean hasChanged();
	abstract void addController(Controller o);
	abstract void notifyController(Task e);
}
