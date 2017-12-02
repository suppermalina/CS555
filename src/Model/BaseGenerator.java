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
	abstract Task getTask(String type);
	abstract Containers getContainer(String type);
}
