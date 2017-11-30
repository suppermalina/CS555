import org.rosuda.JRI.Rengine;

import RNGPack.Rhelper;
public class Test {
	public static void main (String[] args) {
	    // new R-engine
	    System.out.println(Rhelper.getInstance(10, true, 4));
	}	    
}
