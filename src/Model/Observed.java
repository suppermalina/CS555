/**
 * 
 */
package Model;

import java.util.Set;
import java.util.TimerTask;

import ExecutionalInstances.Coordinator;

/**
 * @author mali
 *
 */
public interface Observed {
	abstract boolean hasChanged();
	abstract void addCoordinator(Coordinator o);
	abstract void notifyCoordinatro(Task e);
}
