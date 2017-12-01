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
	abstract Event getEvent(String type);
	abstract Containers getContainer(String type);
}
