package Model;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

public class Test {
	public static void main(String[] args) {
		double[][] original1 = {{2,3},{4,5},{6,7}};
	    double[][] original2 = {{8,9},{12,13},{14,15}};
	    AbstractPlot originalPlot = new DataSetPlot(original1);
	    originalPlot.setTitle("original1");
	    AbstractPlot originalPlot2 = new DataSetPlot(original2);
	    originalPlot2.setTitle("original2");

	    JavaPlot p = new JavaPlot("/usr/local/bin/gnuplot");

	    p.addPlot(originalPlot);
	    p.newGraph();
	    p.addPlot(originalPlot2);

	    p.plot();
	}

}
