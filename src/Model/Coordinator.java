/**
 * 
 */
package Model;

import java.util.Observer;

/**
 * @author mali
 * Coordinator is the observer. It focuses on the state of each event.
 * Once the state updated, coordinator will be notified by the event
 * accordingly. Then it informs the queue or any one server to take
 * a proper action.
 */
public abstract class Coordinator implements Observer {
	
}
