/**
 * 
 */
package Model;

/**
 * @author mali
 *
 */
public final class StatisticalClock {
	public static final long CURRENTTIME = System.currentTimeMillis() / 1000;
	public static long getTime() {
		return System.currentTimeMillis() / 1000 - CURRENTTIME;
	}
}
