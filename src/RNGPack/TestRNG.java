package RNGPack;
import java.awt.Color;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author mali
 * This class contains main function. 
 * RNG is tested here
 * There are 4 methods in this class introduced as following:
 * getDataFromRNG(): 
 * processData(): This method is designed to sort random numbers for preparing CDF data.
 * The number of division points is given based on the size of the sequence.A HashMap is 
 * employed with the value being a division point and the key being the amount of the 
 * random numbers <= a division point
 * 
 */
public class TestRNG {
	// This defines a range of number should be divided into how many intervals
	private int interval;
	
	// runTimes describes the number of times the RNG().initiator() will be called
	private int runTimes;
	
	
	private SPN distributor;
	
	// For getDataFromRNG
	private List<SPN> receiver;
	
	// For processData()
	private List<Double> demo1;
	
	// For processData()
	private List<HeapWithInformation> minHeapGroup;
	
	// processData()
	private List<Double> xData;
	private List<Integer> yData;
	
	private static RNG generator;
	
	private int maxOfYAxis;

	private Map<Integer, Double> stat;
	private Set<Map.Entry<Integer, Double>> dataSet;
	private PriorityQueue<Map.Entry<Integer, Double>> minHeap;
	private int size;
	
	private void setRunTimes() {
		try {
			System.out.println("How many times you want to run");
			Scanner scanner = new Scanner(System.in);
			runTimes = scanner.nextInt();
			if (runTimes <= 0) {
				System.out.println("Illegal input");
				setRunTimes();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//This method is used to generate one or more random sequences
	private void getDataFromRNG() {
		List<SPN> temp = null;
		receiver = new ArrayList<SPN>();
		setRunTimes();
		try {
			for (int i = 0; i < runTimes; i++) {
				temp = new RNG().initiator();
				for (int j = 0; j < temp.size(); j++) {
					// Receiver contains all SPN objects
					receiver.add(temp.get(j));
				}
			}
			minHeapGroup = new ArrayList<HeapWithInformation>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// This method is designed to sort random numbers for preparing CDF data.
	// The number of division points is given based on the size of a sequence.A HashMap is 
	// employed with the Value being the value of a division point and the key being the 
	// amount of the random numbers <= the value a division point
	private void processData() {
		// The first for loop unpacks List<SPN> receiver
		for (int flag = 0; flag < receiver.size(); flag++) {
			// distributor is an SPN, contains a List<List<Double>> and String nameOfSequence
			distributor = receiver.get(flag);
			// tempList holds the List<List<Double>> in distributor
			List<List<Double>> tempList = distributor.getSequence();
			
			String nameOfSequence = distributor.getName();
			
			// This size describes the length of the List<List<Double>> in distributor
			size = tempList.size();
			
			// The second for loop targets to unpack the distributor
			for (int i = 0; i < size; i++) {
				demo1 = tempList.get(i);
				maxOfYAxis = demo1.size();
				demo1.sort(new Comparator<Double>() {
					@Override
					public int compare(Double o1, Double o2) {
						if (o1 < o2) {
							return -1;
						} else if (o1 > o2) {
							return 1;
						} else return 0;
					}
				});
				// The the number of intervals should be set based on the size of a sequence
				if (demo1.size() <= 100) {
					interval = 20;
				} else if (demo1.size() <= 10000) {
					interval = demo1.size() / 10;
				} else {
					interval = 1000;
				}
				
				
				// Length of one interval
				double unit = 1 / (double)interval;
				
				int index = 1;
				int counter = 0;
				stat = new HashMap<Integer, Double>();
				
				// index is used to identify on which interval the program is working
				while (index <= interval) {
					
					// Suppose there are 100 intervals, then one unit should be 0.01
					// So index = 1-100, index increases in each outer loop
					// If index = 50, that means the computer is counter random variables
					// which are smaller than 50 * 0.01 = 0.5
					double demarcation = index * unit;
					while (counter < demo1.size() && demo1.get(counter) <= demarcation) {
						counter++;
					}
					// counter here is somehow a "global" value
					stat.put(counter, demarcation);
					index++;
				}
				
				dataSet = stat.entrySet();
				minHeap = new PriorityQueue<Map.Entry<Integer, Double>>(interval, new Comparator<Map.Entry<Integer, Double>>() {
					// This override sorts each interval based on the amount of random variables it contains
					@Override
					public int compare(Entry<Integer, Double> entryOne, Entry<Integer, Double> entryTwo) {
						if (entryOne.getKey() < entryTwo.getKey()) {
							return -1;
						} else if (entryOne.getKey() > entryTwo.getKey()) {
							return 1;
						} else return 0;
					}
					
				});
				for (Entry<Integer, Double> entry : dataSet) {
					minHeap.offer(entry);
				}
				minHeapGroup.add(new HeapWithInformation(minHeap, nameOfSequence));
			}
		}
	}
	
	private void prepareCDF(PriorityQueue<Map.Entry<Integer, Double>> minHeap) {
		xData = new ArrayList<Double>();
		yData = new ArrayList<Integer>();
		while (!minHeap.isEmpty()) {
			Entry<Integer, Double> temp = minHeap.poll();
			xData.add(temp.getValue());
			yData.add(temp.getKey());
		}
	}
	
	/*private void plotting() {
		getDataFromRNG();
	    processData();
		List<XYChart> charts = new ArrayList<XYChart>();
		int matrixSize = minHeapGroup.size();
	    for (int j = 0; j < matrixSize; j++) {
	    	HeapWithInformation temp = minHeapGroup.get(j);
	    	minHeap = temp.getMinHeap();
	    	String nameOfSequence = temp.getName();
	    	prepareCDF(minHeap);
		    XYChart chart = new XYChartBuilder().xAxisTitle("Index").yAxisTitle("RandomValue").width(600).height(400).build();
		    chart.getStyler().setYAxisMax((double)maxOfYAxis);
		   	XYSeries series = chart.addSeries(nameOfSequence, xData, yData);
		   	series.setMarker(SeriesMarkers.CIRCLE);
		   	series.setMarkerColor(Color.RED);
		   	series.setLineColor(Color.BLACK);
	    	series.setLineWidth(0.3f);
	    	charts.add(chart);
	    }
	    new SwingWrapper<XYChart>(charts).displayChartMatrix();
	 }*/
	
	
	class HeapWithInformation {
		private PriorityQueue<Map.Entry<Integer, Double>> minHeap;
		private String nameOfSequence;
		
		HeapWithInformation(PriorityQueue<Map.Entry<Integer, Double>> minHeap, String nameOfSequence) {
			this.minHeap = minHeap;
			this.nameOfSequence = nameOfSequence;
		}
		
		private PriorityQueue<Map.Entry<Integer, Double>> getMinHeap() {
			return minHeap;
		}
		
		private String getName() {
			return nameOfSequence;
		}
	}
	
	public static void main(String[] args) {
		new TestRNG().plotting();
	}
}
