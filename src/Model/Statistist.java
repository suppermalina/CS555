package Model;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.RgbPlotColor;
import com.panayotis.gnuplot.style.Style;

public class Statistist {
	private Controller controller;
	private double totalServicedCustomer;
	private double totalRejectedCustomer;
	private double[][] systemGNU;
	private double[][] queueGNU;
	private double[][] serverGNU;
	private double[][] servicedQuantityGNU;
	private double[][] rejectedQuantityGNU;


	private double[][] averageTimeInSystemGNU;
	private double[][] averageTimeInQueueGNU;
	private double[][] averageTimeInServerGNU;
	private double[][] averageServicedQuantityGNU;
	private double[][] averageRejectedQuantityGNU;

	private double runTimes;
	private double[][] dataForGNU;
	private double[][] averageDataForGNU;

	private Deque<double[][]> information;
	private Deque<double[][]> averageInformation;
	private Deque<Deque<Information>> allInform;
	private Deque<Information> systemInform;
	private Deque<Information> queueInform;
	private Deque<Information> serverInform;
	private Deque<Information> rejectedInform;

	private int arraySize = (int) (Controller.endingTime / Controller.period) - 1;
	private final String pathToGNU = "/usr/local/bin/gnuplot";
	private ArrayList<String> periodPlotY;
	private ArrayList<String> averagePlotY;

	private FileWriter systemInformoutputPath;
	private BufferedWriter systemInformWriter;

	private FileWriter queueInformOutputPath;
	private BufferedWriter queueInformWriter;

	private FileWriter serverInformOutputPath;
	private BufferedWriter serverInformWriter;

	private FileWriter servicedCustomerQuantityOutputPath;
	private BufferedWriter servicedCustomerQuantityWriter;

	private FileWriter rejectedCustomerQuantityOutputPath;
	private BufferedWriter rejectedCustomerQuantityWriter;

	private FileWriter totalRatioOutputPath;
	private BufferedWriter totalRatioWriter;

	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	private void setUpArrays() {
		information = new LinkedList<double[][]>();
		averageInformation = new LinkedList<double[][]>();

		information.offerLast(systemGNU = new double[arraySize][2]);
		information.offerLast(queueGNU = new double[arraySize][2]);
		information.offerLast(serverGNU = new double[arraySize][2]);
		information.offerLast(servicedQuantityGNU = new double[arraySize][2]);
		information.offerLast(rejectedQuantityGNU = new double[arraySize][2]);

		averageInformation.offerLast(averageTimeInSystemGNU = new double[arraySize][2]);
		averageInformation.offerLast(averageTimeInQueueGNU = new double[arraySize][2]);
		averageInformation.offerLast(averageTimeInServerGNU = new double[arraySize][2]);
		averageInformation.offerLast(averageServicedQuantityGNU = new double[arraySize][2]);
		averageInformation.offerLast(averageRejectedQuantityGNU = new double[arraySize][2]);
		
		periodPlotY = new ArrayList<String>();
		periodPlotY.add("TimeInSystemPeriodRecord");
		periodPlotY.add("TimeInQueuePeriodRecord");
		periodPlotY.add("TimeInServerPeriodRecord");
		periodPlotY.add("PeriodServicedQuantity");
		periodPlotY.add("PeriodRejectedQuantity");
	

		averagePlotY = new ArrayList<String>();
		averagePlotY.add("AverageTimeInSystem");
		averagePlotY.add("AverageTimeInQueue");
		averagePlotY.add("AverageTimeInServer");
		averagePlotY.add("AverageServicedCustomerQuantity");
		averagePlotY.add("AverageRejectedCustomerQuantity");
	}

	private void startUp() {
		controller = Controller.getInstance();
		boolean flag = controller.start();
		if (flag == true) {
			fetchData(controller.model.pitcher());
		}
	}

	private void fetchData(Deque<Deque<Information>> pitcher) {
		Deque<Information> totalStatisticalData = pitcher.pollLast();
		while (!totalStatisticalData.isEmpty()) {
			totalServicedCustomer += totalStatisticalData.pollFirst().getAverageTime();
			totalRejectedCustomer += totalStatisticalData.pollFirst().getAverageTime();
		}
		while (!pitcher.isEmpty()) {
			Deque<Information> temp = pitcher.pollFirst();
			dataForGNU = information.pollFirst();
			averageDataForGNU = averageInformation.pollFirst();
			System.out.println(temp == null);
			System.out.println(temp.isEmpty());
			int index = 0;
			double avg = 0;
			double time = 0;
			double sumAvg = 0;
			while (!temp.isEmpty()) {
				Information inform = temp.pollFirst();
				avg = inform.getAverageTime();
				time = inform.getTime();
				sumAvg += avg;
				dataForGNU[index][1] += avg / runTimes;
				dataForGNU[index][0] += time / runTimes;
				averageDataForGNU[index][0] += time / runTimes;
				index++;
			}
			if (sumAvg != 0) {
				System.out.println("-------------------sumAvg is: " + sumAvg + "---------------------------");
				System.out.println("-------------------sumAvg / (runTimes * arraySize) is: "
						+ sumAvg / (runTimes * arraySize) + "---------------------------");
				for (int i = 0; i < averageDataForGNU.length; i++) {
					averageDataForGNU[i][1] += sumAvg / (runTimes * arraySize);
				}
			}
			information.offerLast(dataForGNU);
			averageInformation.offerLast(averageDataForGNU);
		}
	}

	public boolean dataReporterReady() {
		try {
			systemInformoutputPath = new FileWriter(
					new File("/Users/mali/Desktop/555Project/data&report/systemInform.csv"));
			systemInformWriter = new BufferedWriter(systemInformoutputPath);

			queueInformOutputPath = new FileWriter(
					new File("/Users/mali/Desktop/555Project/data&report/queueInform.csv"));
			queueInformWriter = new BufferedWriter(queueInformOutputPath);

			serverInformOutputPath = new FileWriter(
					new File("/Users/mali/Desktop/555Project/data&report/serverInform.csv"));
			serverInformWriter = new BufferedWriter(serverInformOutputPath);

			servicedCustomerQuantityOutputPath = new FileWriter(
					new File("/Users/mali/Desktop/555Project/data&report/servicedCustomerQuantityInform.csv"));
			servicedCustomerQuantityWriter = new BufferedWriter(servicedCustomerQuantityOutputPath);

			rejectedCustomerQuantityOutputPath = new FileWriter(
					new File("/Users/mali/Desktop/555Project/data&report/rejectedCustomerQuantityInform.csv"));
			rejectedCustomerQuantityWriter = new BufferedWriter(rejectedCustomerQuantityOutputPath);

			totalRatioOutputPath = new FileWriter(
					new File("/Users/mali/Desktop/555Project/data&report/totalRatio.csv"));
			totalRatioWriter = new BufferedWriter(totalRatioOutputPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private boolean generateReport() {

		for (int i = 0; i < systemGNU.length; i++) {
			try {
				this.systemInformWriter.append(String.valueOf(systemGNU[i][0]));
				this.serverInformWriter.append(COMMA_DELIMITER);
				this.systemInformWriter.append(String.valueOf(systemGNU[i][1]));
				this.systemInformWriter.append(NEW_LINE_SEPARATOR);

				this.queueInformWriter.append(String.valueOf(queueGNU[i][0]));
				this.queueInformWriter.append(COMMA_DELIMITER);
				this.queueInformWriter.append(String.valueOf(queueGNU[i][1]));
				this.queueInformWriter.append(NEW_LINE_SEPARATOR);

				this.serverInformWriter.append(String.valueOf(serverGNU[i][0]));
				this.serverInformWriter.append(COMMA_DELIMITER);
				this.serverInformWriter.append(String.valueOf(serverGNU[i][1]));
				this.serverInformWriter.append(NEW_LINE_SEPARATOR);

				this.servicedCustomerQuantityWriter.append(String.valueOf(servicedQuantityGNU[i][0]));
				this.servicedCustomerQuantityWriter.append(COMMA_DELIMITER);
				this.servicedCustomerQuantityWriter.append(String.valueOf(servicedQuantityGNU[i][1]));
				this.servicedCustomerQuantityWriter.append(NEW_LINE_SEPARATOR);

				this.rejectedCustomerQuantityWriter.append(String.valueOf(rejectedQuantityGNU[i][0]));
				this.rejectedCustomerQuantityWriter.append(COMMA_DELIMITER);
				this.rejectedCustomerQuantityWriter.append(String.valueOf(rejectedQuantityGNU[i][1]));
				this.rejectedCustomerQuantityWriter.append(NEW_LINE_SEPARATOR);

			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				this.totalRatioWriter.append("totalServiced");
				this.totalRatioWriter.append(COMMA_DELIMITER);
				this.totalRatioWriter.append("totalRejected");
				this.totalRatioWriter.append(NEW_LINE_SEPARATOR);
				this.totalRatioWriter.append(String.valueOf(this.totalServicedCustomer));
				this.totalRatioWriter.append(COMMA_DELIMITER);
				this.totalRatioWriter.append(String.valueOf(this.totalRejectedCustomer));
				this.totalRatioWriter.append(NEW_LINE_SEPARATOR);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public void flushAll() {
		try {
			systemInformWriter.flush();
			queueInformWriter.flush();
			serverInformWriter.flush();
			servicedCustomerQuantityWriter.flush();
			

			rejectedCustomerQuantityWriter.flush();
	
			totalRatioWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void closeAllLog() {
		try {
			systemInformWriter.close();
			queueInformWriter.close();
			serverInformWriter.close();
			servicedCustomerQuantityWriter.close();
			

			rejectedCustomerQuantityWriter.close();
			

			totalRatioWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean plotting() {
		JavaPlot plotter = null;
		for (int i = 0; i < 5; i++) {
			PlotStyle style1 = new PlotStyle();
			PlotStyle style2 = new PlotStyle();
			// style1.set("terminal qt", "7");
			style1.setLineType(9);
			style2.setLineType(9);

			dataForGNU = information.pollFirst();
			averageDataForGNU = averageInformation.pollFirst();
			for (int j = 0; j < averageDataForGNU.length; j++) {
				for (int k = 0; k < averageDataForGNU[j].length; k++) {
					System.out.print(averageDataForGNU[j][k] + " ");
				}
				System.out.println();
			}
			AbstractPlot pl1 = new DataSetPlot(dataForGNU);
			AbstractPlot pl2 = new DataSetPlot(averageDataForGNU);
			pl1.setTitle(this.periodPlotY.get(i));
			pl2.setTitle(this.averagePlotY.get(i));
			RgbPlotColor color1 = new RgbPlotColor(249, 18, 18);
			RgbPlotColor color2 = new RgbPlotColor(19, 46, 249);
			style1.setPointSize(1);
			style1.setStyle(Style.LINESPOINTS);
			style1.setLineType(color1);

			style2.setPointSize(1);
			style2.setStyle(Style.LINES);
			style2.setLineType(19);
			style2.setLineType(color2);
			pl1.setPlotStyle(style1);
			pl2.setPlotStyle(style2);

			plotter = new JavaPlot(pathToGNU);
			plotter.addPlot(pl1);
			plotter.addPlot(pl2);
			plotter.plot();
		}
		return true;
	}

	public static void main(String[] args) {
		Statistist expert = new Statistist();
		expert.runTimes = 100.0;
		expert.allInform = new LinkedList<Deque<Information>>();
		expert.setUpArrays();
		for (int i = 0; i < expert.runTimes; i++) {
			expert.startUp();
		}
		boolean initiateReporter = expert.dataReporterReady();
		boolean reportFlag = false;
		if (initiateReporter) {
			reportFlag = expert.generateReport();
		}
		if (reportFlag) {
			expert.flushAll();
			expert.closeAllLog();
		}

		boolean flag = expert.plotting();
		if (flag) {
			expert.controller.systemShutDown();
		}
	}
}
