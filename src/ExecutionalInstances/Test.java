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
	public static void main(String[] args) {
		Center c = Center.getInstance();
		c.start();
	}
}
