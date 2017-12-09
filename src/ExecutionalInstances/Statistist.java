package ExecutionalInstances;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.RgbPlotColor;
import com.panayotis.gnuplot.style.Style;

import EventsInstances.Customer;
import EventsInstances.GenerateCustomer;
import EventsInstances.PopCustomerOut;
import Model.Information;
import Model.Task;

public class Statistist {
	public static long averageSignalInitiatingTime;
	private static Controller controller;
	private static Map<Set<Customer>, Integer> rejectStat;
	private static List<List<Integer>> catcher;
	private static ArrayList<ArrayList<Double>> XAxis;
	private static ArrayList<ArrayList<Integer>> YAxis;
	private double[][] systemGNU;
	private double[][] queueGNU;
	private double[][]serverGNU;
	private double[][] rejectedGNU;
	private Deque<double[][]> information;
	private Deque<Deque<Information>> allInform;
	private Deque<Information> systemInform;
	private Deque<Information> queueInform;
	private Deque<Information> serverInform;
	private Deque<Information> rejectedInform;
	private double runTimes;
	private double[][] dataForGNU;
	private int arraySize = (int) (Controller.endingTime / Controller.period) - 1;
	private final String pathToGNU = "/usr/local/bin/gnuplot";
	private ArrayList<String> yLabel;
	private RgbPlotColor color;

	private void setUpArrays() {
		information = new LinkedList<double[][]>();
		information.offerLast(systemGNU = new double[arraySize][2]);
		information.offerLast(queueGNU = new double[arraySize][2]);
		information.offerLast(serverGNU = new double[arraySize][2]);
		information.offerLast(rejectedGNU = new double[arraySize][2]);
		yLabel = new ArrayList<String>();
		yLabel.add("SystemState");
		yLabel.add("ServerState");
		yLabel.add("QueueState");
		yLabel.add("RejectedState");
	}

	private void startUp() {
		controller = Controller.getInstance();
		boolean flag = controller.start();
		System.out.println(flag);
		while (flag) {
			controller.reporter.flushAll();
			controller.reporter.closeAllLog();
			flag = false;
		}
		System.out.println(flag);
		Deque<Deque<Information>> temp = Controller.model.pitcher();
		Deque<Information> test = temp.pollFirst();
		/*while (!test.isEmpty()) {
			Information infor = test.pollFirst();
			System.out.println(infor.getAverageTime());
			System.out.println(infor.getTime());
		}*/
		fetchData(controller.model.pitcher());
	}
	
	/*private void prepareDataForMean(Deque<Deque<Information>> pitcher) {
		System.out.println(pitcher.size());
		while(!pitcher.isEmpty()) {
			systemInform = pitcher.pollFirst();
			allInform.add(systemInform);
			queueInform = pitcher.pollFirst();
			allInform.add(1, queueInform);
			serverInform = pitcher.pollFirst();
			allInform.add(2, serverInform);
			rejectedInform = pitcher.pollFirst();
			allInform.add(3, rejectedInform);
		}
		System.out.println(allInform.size());
		fetchData();
	}*/
	
	private void fetchData(Deque<Deque<Information>> pitcher) {
		/*for (int i = 0; i < allInform.size(); i++) {
			
			Deque<Information> temp = allInform.get(i);
			System.out.println(temp == null);
			int index = 0;
			while (!temp.isEmpty()) {
				Information inform = temp.pollFirst();
				double avgTime = inform.getAverageTime();
				double timeStamp = inform.getTime();
				dataForGNU[index][1] += avgTime / runTimes;
				dataForGNU[index][0] += timeStamp / runTimes;
				index++;
			}
		}*/
		
		while (!pitcher.isEmpty()) {
			Deque<Information> temp = pitcher.pollFirst();
			double[][] dataForGNU = information.pollFirst();
			System.out.println(temp == null);
			System.out.println(temp.isEmpty());
			int index = 0;
			while (!temp.isEmpty()) {
				Information inform = temp.pollFirst();
				double avgTime = inform.getAverageTime();
				double timeStamp = inform.getTime();
				dataForGNU[index][1] += avgTime / runTimes;
				dataForGNU[index][0] += timeStamp / runTimes;
				index++;
			}
			information.offerLast(dataForGNU);
		}
	}
	
	/*private void testInfor() {
		
		Deque<Information> temp = allInform.get(0);
		//double[] tempX = information.get(0);
		//double[] tempY = information.get(1);
		int j = 0;
		System.out.println(temp.isEmpty());
		while (!temp.isEmpty()) {
			Information inform = temp.pollFirst();
			double avgTime = inform.getAverageTime();
			double timeStamp = inform.getTime();
			System.out.println("testx is " + avgTime + ", testy is " + timeStamp);
			systemInformY[j] += avgTime / runTimes;
			systemInformX[j] += timeStamp / runTimes;
			j++;
		}*/
	
	
	private boolean plotting() {
		JavaPlot plotter = null;
		/*try {
			plotter.setGNUPlotPath(pathToGNU);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		for (int i = 0; i < 4; i++) {
			plotter = new JavaPlot(pathToGNU);
			PlotStyle style = new PlotStyle();
			style.set("terminal qt", "4");
			style.setLineType(9);
			dataForGNU = information.pollFirst();
			for (int j = 0; j < dataForGNU.length; j++) {
				for (int k = 0; k < dataForGNU[j].length; k++) {
					System.out.print(dataForGNU[j][k] + " ");
				}
				System.out.println();
			}
			DataSetPlot pl = new DataSetPlot(dataForGNU);
			color = new RgbPlotColor(10 * i, 20 * i, 30 * i);
			plotter.setTitle(this.yLabel.get(i));
			style.setPointSize(i);
			style.setStyle(Style.LINESPOINTS);
			style.setLineType(color);
			pl.setPlotStyle(style);
			plotter.addPlot(pl);
			plotter.plot();
		}
		return true;
	}

	public static void main(String[] args) {
		Statistist expert = new Statistist();
		expert.runTimes = 10.0;
		expert.allInform = new LinkedList<Deque<Information>>();
		expert.setUpArrays();
		
		for (int i = 0; i < expert.runTimes; i++) {
			expert.startUp();
		}
		boolean flag = expert.plotting();
		if(flag) {
			controller.systemShutDown();
		}
	}
}
