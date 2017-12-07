package ExecutionalInstances;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

import ContainersInstance.StateList;
import ContainersInstance.TaskList;
import EventsInstances.Customer;
import EventsInstances.GenerateCustomer;
import EventsInstances.PopCustomerOut;
import Model.SystemState;
import Model.Task;

public class Statistist {
	public static long averageSignalInitiatingTime;
	private static Controller controller;
	private static Map<Set<Customer>, Integer> rejectStat;
	private static List<List<Integer>> catcher;
	private static ArrayList<ArrayList<Double>> XAxis;
	private static ArrayList<ArrayList<Integer>> YAxis;	
	private static double[] xData;
	private static double[] yData;
	//private static ArrayList<ArrayList<Double>> getTime = new ArrayList<ArrayList<Double>>();
	private static PriorityQueue<ArrayList<Double>>	minHeapCustomer = new PriorityQueue<ArrayList<Double>>(10, new Comparator<ArrayList<Double>>() {

		@Override
		public int compare(ArrayList<Double> listOne, ArrayList<Double> listTwo) {
			// TODO Auto-generated method stub
			if (listOne.size() > listTwo.size()) {
				return -1;
			} else if (listOne.size() < listTwo.size()) {
				return 1;
			} else return 0;
		}
	});
	
	private static PriorityQueue<ArrayList<Double>>	minHeapTime = new PriorityQueue<ArrayList<Double>>(10, new Comparator<ArrayList<Double>>() {

		@Override
		public int compare(ArrayList<Double> listOne, ArrayList<Double> listTwo) {
			// TODO Auto-generated method stub
			if (listOne.size() > listTwo.size()) {
				return -1;
			} else if (listOne.size() < listTwo.size()) {
				return 1;
			} else return 0;
		}
	});
	
	
	// Due to the low accuracy of Java timer, each sequence may has a different size
	// We will use the smallest size
	private static void prepareDataForMean(ArrayList<ArrayList<Double>> pitcher) {
		System.out.println(pitcher.size());
		minHeapTime.offer(pitcher.get(0));
		minHeapCustomer.offer(pitcher.get(1));
		pitcher = null;
	}
	
	private static void calculateMean() {
		int commonSize = minHeapCustomer.peek().size();
		int numberOfRuns = minHeapTime.size();
		//testPrint(minHeap.peek(), getTime.get(0));
		xData = new double[commonSize];
		yData = new double[commonSize];
		double base = (double)numberOfRuns;
		while (!minHeapTime.isEmpty() && !minHeapCustomer.isEmpty()) {
			ArrayList<Double> tempX = minHeapTime.poll();
			ArrayList<Double> tempY = minHeapCustomer.poll();
			int limit = tempX.size();
			for (int i = 0; i < limit; i++) {
				xData[i] += tempX.get(i) / base;
				yData[i] += tempY.get(i) / base;
			}
		}
		//testPrint(xData, yData);
	}
	

	private static void startUp() {
		controller = new Controller();
		boolean flag = controller.start();
		System.out.println(flag);
		while (flag) {
			controller.reporter.flushAll();
			controller.reporter.closeAllLog();
			flag = false;
		}
		System.out.println(flag);
		prepareDataForMean(controller.simulator.sendData());
	}

	private static void getData() {
		rejectStat = controller.simulator.rejectStatData();
		XAxis = controller.simulator.XAxis;
		YAxis = controller.simulator.YAxis;
	}

	private static void shutDown() {
		controller.systemShutDown();
	}
	
	private static void testPrint(ArrayList<Double> one, ArrayList<Double> two) {
		for (int i = 0; i < one.size(); i++) {
			System.out.println("X value: " + one.get(i) + ", Y value: " + two.get(i));
		}
		//System.out.println("This sequence has total: " + XAxis.get(0).size() + " data");
	}
	
	private static void singlePlot() {
		List<XYChart> charts = new ArrayList<XYChart>();
		//xData = Controller.simulator.currentStateX;
    	//yData = Controller.simulator.currentStateY;
    	XYChart chart = new XYChartBuilder().xAxisTitle("Avg Sampling time").yAxisTitle("Avg Number of customers in the system").width(1200).height(800).build();
	    chart.getStyler().setYAxisMax(10.0);
	   	XYSeries series = chart.addSeries("State", xData, yData);
	   	series.setMarker(SeriesMarkers.CIRCLE);
	   	series.setMarkerColor(Color.RED);
	   	series.setLineColor(Color.BLACK);
    	series.setLineWidth(0.3f);
    	new SwingWrapper<XYChart>(chart).displayChart();
	}
	
	private static void plotting() {
		List<XYChart> charts = new ArrayList<XYChart>();
		int matrixSize = XAxis.size();
	    for (int i = 0; i < matrixSize; i++) {
	    	ArrayList<Double> xData = XAxis.get(i);
	    	ArrayList<Integer> yData = YAxis.get(i);
	    	String xTitle = null;
	    	String yTitle = null;
	    	String nameOfSequence = null;
	    	double xAxisInfor = xData.remove(0);
	    	int yAxisInfor = yData.remove(0);
	    	if (xAxisInfor == 0 && yAxisInfor == 0) {
	    		xTitle = "Exist Customer CDF";
	    		yTitle = "Exist Customer CDF";
	    		nameOfSequence = "ExistCustomerCDF";
	    	} else if (xAxisInfor == 1 && yAxisInfor == 1) {
	    		xTitle = "Customer In Que";
	    		yTitle = "Customer In Que";
	    		nameOfSequence = "ExistCustomerInQue";
	    	} else if (xAxisInfor == 2 && yAxisInfor == 2) {
	    		xTitle = "Customer in service CDF";
	    		yTitle = "Customer in service Customer CDF";
	    		nameOfSequence = "ServiceCDF";
	    	} else {
	    		xTitle = "Customer in service";
	    		yTitle = "Customer in service";
	    		nameOfSequence = "Customers in Service";
	    	}
		    XYChart chart = new XYChartBuilder().xAxisTitle(xTitle).yAxisTitle(yTitle).width(600).height(400).build();
		    chart.getStyler().setYAxisMax(60.0);
		   	XYSeries series = chart.addSeries(nameOfSequence, xData, yData);
		   	series.setMarker(SeriesMarkers.CIRCLE);
		   	series.setMarkerColor(Color.RED);
		   	series.setLineColor(Color.BLACK);
	    	series.setLineWidth(0.3f);
	    	charts.add(chart);
	    }
	    new SwingWrapper<XYChart>(charts).displayChartMatrix();
	 }
	
	

	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {
			startUp();
		}
		
		
		// getData();
		//testPrint();
		calculateMean();
		singlePlot();
		//shutDown();
		
	}
}
