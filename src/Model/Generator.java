/**
 * 
 */
package Model;

import java.util.*;

/**
 * @author mali
 *
 */
public interface Generator {
	abstract Event generateEvent(int ID, String type);
	abstract Collection getContainer(String type);
	abstract double getInterval(long rate);
	abstract double getRandom();
}
