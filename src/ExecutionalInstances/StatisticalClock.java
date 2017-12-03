/**
 * 
 */
package ExecutionalInstances;

/**
 * @author mali
 * This is the singleton here. 
 * The whole program only need one timer.
 */
public final class StatisticalClock {
	private final static long START = System.currentTimeMillis();
	private static long CURRENT = System.currentTimeMillis();
	
    public static synchronized long CLOCK() {
        return System.currentTimeMillis()  - START;
    }
}
