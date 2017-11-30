import java.awt.Color;
import java.util.*;
import org.knowm.xchart.*;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.markers.SeriesMarkers;

import RNGPack.RNG;

public class TestX {
	private static List<Double> input;
	private static List<Integer> xData;
	public static void main(String[] args) throws Exception {
		input = (List<Double>) new RNG().initiator().get(0);
		xData = new ArrayList<Integer>();
		for (int i = 0; i < input.size(); i++) {
			xData.add(i);
		}
		XYChart chart = new XYChartBuilder().xAxisTitle("Index").yAxisTitle("RandomValue").width(1000).height(800).build();
		chart.getStyler().setYAxisMax(2.0);
	    XYSeries series = chart.addSeries("test", xData, input);
	    series.setMarker(SeriesMarkers.CIRCLE);
	    series.setMarkerColor(Color.RED);
	    series.setLineColor(Color.BLACK);
	    series.setLineWidth(0.3f);
	    new SwingWrapper(chart).displayChart();
	}
}
