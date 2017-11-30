import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.line.LineChart03;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class TestXX {
	 public static void main(String[] args) {

		    int numCharts = 4;

		    List<XYChart> charts = new ArrayList<XYChart>();

		    for (int i = 0; i < numCharts; i++) {
		      XYChart chart = new XYChartBuilder().xAxisTitle("X").yAxisTitle("Y").width(600).height(400).build();
		      chart.getStyler().setYAxisMin(-10.0);
		      chart.getStyler().setYAxisMax(10.0);
		      XYSeries series = chart.addSeries("" + i, null, getRandomWalk(200));
		      series.setMarker(SeriesMarkers.NONE);
		      charts.add(chart);
		    }
		    new SwingWrapper<XYChart>(charts).displayChartMatrix();
		  }

		  /**
		   * Generates a set of random walk data
		   *
		   * @param numPoints
		   * @return
		   */
		  private static double[] getRandomWalk(int numPoints) {

		    double[] y = new double[numPoints];
		    y[0] = 0;
		    for (int i = 1; i < y.length; i++) {
		      y[i] = y[i - 1] + Math.random() - .5;
		    }
		    return y;
		  }

}
