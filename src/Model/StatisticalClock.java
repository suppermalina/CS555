/**
 * 
 */
package Model;

/**
 * @author mali
 * This is the singleton here. 
 * The whole program only need one timer.
 */
public final class StatisticalClock {
	private final long START = System.currentTimeMillis();
	//private static long CURRENT = System.currentTimeMillis();
	private final long NANOSTART = System.nanoTime();
    public synchronized long CLOCK() {
        return System.currentTimeMillis()  - START;
    }
    
    private StatisticalClock instance = null;
    
    public synchronized StatisticalClock getInstance() {
    	if (instance == null) {
    		instance = new StatisticalClock();
    	}
    	return instance;
    }
    
    public synchronized long NANOCLOCK() {
    	return System.nanoTime() - NANOSTART;
    }
}
