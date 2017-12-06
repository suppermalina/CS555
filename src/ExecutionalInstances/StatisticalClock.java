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
	private final static long NANOSTART = System.nanoTime();
    public static synchronized long CLOCK() {
        return System.currentTimeMillis()  - START;
    }
    
    public static synchronized long NANOCLOCK() {
    	return System.nanoTime() - NANOSTART;
    }
}
