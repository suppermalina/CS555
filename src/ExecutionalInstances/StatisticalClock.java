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
	private static final long CURRENTTIME = System.currentTimeMillis() / 1000;

    public static final synchronized long CLOCK() {
        return System.currentTimeMillis() / 1000 - CURRENTTIME;
    }
}
