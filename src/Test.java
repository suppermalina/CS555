import java.awt.Graphics;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.rosuda.JRI.Rengine;
import com.panayotis.*;
import com.panayotis.gnuplot.GNUPlot;
import com.panayotis.gnuplot.GNUPlotParameters;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.PropertiesHolder;
import com.panayotis.gnuplot.dataset.ArrayDataSet;
import com.panayotis.gnuplot.dataset.DataSet;
import com.panayotis.gnuplot.plot.Axis;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.plot.Plot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.swing.JPlot;

import ExecutionalInstances.Controller;
import ExecutionalInstances.RandomNumberGenerator;
import ExecutionalInstances.StatisticalClock;
import RNGPack.Rhelper;

public class Test {
	ArrayDataSet testSet;
	int[][] testData;

	public boolean setDataSet() {
		testData = new int[99][2];
		for (int i = 0; i < testData.length; i++) {
			int x = i;
			int y = i * 2;
			testData[i][0] = x;
			testData[i][1] = y;

		}
		for (int i = 0; i < testData.length; i++) {
			System.out.println(testData[i][0]);
			System.out.println(testData[i][1]);

		}
		return true;
	}

	public static void main(String[] args) {
		Test t = new Test();
		t.setDataSet();
		JavaPlot plotter = new JavaPlot();
		DataSetPlot pl = new DataSetPlot(t.testData);
		PlotStyle style = new PlotStyle();
		style.set("terminal qt", "2");
		style.set("ylabel", "for fun");
		style.setLineType(9);
		style.setPointSize(1);
		style.setStyle(Style.LINESPOINTS);
		pl.setPlotStyle(style);
		plotter.addPlot(pl);
		
		try {
			plotter.setGNUPlotPath("/usr/local/bin/gnuplot");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 2; i++) {
			plotter.plot();
		}
	}
}
