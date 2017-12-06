package ExecutionalInstances;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private static ArrayList<ArrayList<Double>> XAxis;
	private static ArrayList<ArrayList<Integer>> YAxis;	

	private static void startUp() {
		controller = Controller.getInstance();
		boolean flag = controller.start();
		System.out.println(flag);
		if (flag) {
			controller.reporter.flushAll();
			controller.reporter.closeAllLog();
		}
	}

	private static void getData() {
		rejectStat = controller.simulator.rejectStatData();
		XAxis = controller.simulator.XAxis;
		YAxis = controller.simulator.YAxis;
	}

	private static void shutDown() {
		controller.systemShutDown();
	}
	
	private static void testPrint(ArrayList xList, ArrayList yList) {
		for (int i = 0; i < xList.size(); i++) {
			System.out.println("X value: " + xList.get(i) + ", Y value: " + yList.get(i));
		}
		System.out.println("This sequence has total: " + XAxis.get(0).size() + " data");
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
		startUp();
		getData();
		//testPrint();
		plotting();
		//shutDown();
		
	}
}
