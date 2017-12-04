import java.util.*;
import java.util.concurrent.TimeUnit;

import org.rosuda.JRI.Rengine;

import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;
import RNGPack.Rhelper;
public class Test {
	public static Deque list;
	public static void main (String[] args) {
	    // new R-engine
		System.out.println("Start at: " + StatisticalClock.CLOCK());
		Thread test = new Thread(new TestTimer());
		test.run();
		System.out.println("End at: " + StatisticalClock.CLOCK());
		System.out.println(TimeUnit.MILLISECONDS.toMillis((long) (RandomNumberGenerator.getInstance(0.5) * 1000)));
	}	    
}
