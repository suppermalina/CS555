package ExecutionalInstances;

import ContainersInstance.StateList;
import ContainersInstance.TaskList;

public final class TestGer {
	public static TaskList log = (TaskList) Generator.getContainer("tasklist");
	public static void main(String[] args) {
		System.out.println(new TestGer().log.getName());
	}

}
