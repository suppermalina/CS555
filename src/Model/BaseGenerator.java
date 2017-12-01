/**
 * 
 */
package Model;

import java.util.*;

/**
 * @author mali
 * This is the abstract factory for gener
 */
public interface BaseGenerator {
	abstract Event getEvent(String type, int ID);
	abstract Containers getContainer(String type, int ID);
	abstract double getInterval(long rate);
	abstract double getRandom();
}
