package ExecutionalInstances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
	private static ArrayList<ArrayList<Long>> XAxis;
	private static ArrayList<ArrayList<Integer>> YAxis;

	private static void setUp() {
		
	}
	
	


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
		XAxis = Controller.simulator.XAxis;
		YAxis = Controller.simulator.YAxis;
	}

	private static void shutDown() {
		controller.systemShutDown();
	}
	
	private static void testPrint() {
		for (int i = 0; i < XAxis.get(0).size(); i++) {
			System.out.println(XAxis.get(0).get(i));
		}
	}

	private static void splitData(ArrayList<Map<Long, Integer>> list) {
		
	}

	public static void main(String[] args) {
		startUp();
		getData();
		testPrint();
		shutDown();
		
	}
}
