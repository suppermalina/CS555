package RNGPack;

import java.util.*;

import org.rosuda.JRI.Rengine;

public final class Rhelper {
	private static List<Double> list;
	private final static Rengine ENGINE = new Rengine(new String[] {"--vanilla"}, false, null);
    
    public static void setSeed(long seed) {
    	ENGINE.eval("set.seed(" + seed + ")");
    }
    
    public static List<Double> getInstance(int n, boolean flag, double seed) {
    	list = new ArrayList<Double>();
    	
        if (flag == true) {
        	ENGINE.eval("set.seed(" + seed + ")");
        }
        for (int i = 0; i < n; i++) {
    		list.add(ENGINE.eval("runif(1)").asDouble());
    	}
        return list;
    }
}
