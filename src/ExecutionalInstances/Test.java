package ExecutionalInstances;

import java.util.HashMap;
import java.util.Map;

import ContainersInstance.StateList;
import ContainersInstance.TaskList;
import EventsInstances.GenerateCustomer;
import EventsInstances.PopCustomerOut;
import Model.SystemState;
import Model.Task;

public class Test {
	public static long averageSignalInitiatingTime;
	private static Controller controller;

	public static void main(String[] args) {
		controller = Controller.getInstance();
		controller.start();
		controller.exportLog();
	
		
	}
}
