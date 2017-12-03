package ExecutionalInstances;

import java.util.*;

import ContainersInstance.StateList;
import ContainersInstance.TaskList;
import EventsInstances.GenerateCustomer;
import EventsInstances.PopCustomerOut;
import Model.*;

public final class Center {
	// These two variables used be a part of GenerateCustomer and PopCustomerOut
	// accordingly
	// but I think these two variables should be set and controlled by the
	// controller
	// The average arriving rate
	private double lambda = 0.5;
	// The service rate
	private double miu = 1;
	private long endingTime = 5000;
	private long delay;

	// These two lists were in the Simulator class
	public static StateList log;
	public static TaskList tasks;

	protected Center() {
		log = (StateList) Generator.getContainer("statelist");
		tasks = (TaskList) Generator.getContainer("tasklist");
		System.out.println("Controller ready");
	}

	private static final Center INSTANCE = new Center();

	public static Center getInstance() {
		return INSTANCE;
	}

	private Simulator simulator = Simulator.getInstance();

	private Map<Integer, Task> timerPool = new HashMap<Integer, Task>();

	public synchronized void addTask(Task t) {
		tasks.takeTaskIn(t);
		timerPool.put(t.getId(), t);
	}

	public synchronized void notified(Task t) {
		if (t == null) {
			System.out.println("it's null");
		} else {
			System.out.println("center received the task");
		}
		update(t);
	}

	private boolean messager = false;

	private void signals() {
		while (StatisticalClock.CLOCK() <= endingTime) {
			if (StatisticalClock.CLOCK() == 0 || messager == true) {
				delay = (long) (RandomNumberGenerator.getInstance(lambda) * 1000);
				Generator.intervalForGenerating = delay;
				GenerateCustomer tempCust = (GenerateCustomer) Generator.getTask("generating");
				// System.out.println("Is this gener " + tempCust.getId() + " +
				// " + tempCust.getTerminalTime());
				tasks.takeTaskIn(tempCust);
				// System.out.println(tasks.getSize());
				// System.out.println(StatisticalClock.CLOCK());
			}
		}
		if (tasks.isEmpty()) {
			System.out.println(log);
			System.out.println("Over");
			System.exit(1);
		}
	}

	private synchronized void update(Task t) {
		System.out.println(StatisticalClock.CLOCK());
		if (t == null) {
			// System.out.println("update null");
		} else {
			// System.out.println("update received the task");
		}
		if (t instanceof GenerateCustomer) {
			messager = simulator.generateNewCustomer(t);
		} else {
			System.out.println("pop was called");
			simulator.popCustomer((PopCustomerOut) t);
		}
		SystemState currentState = new SystemState(StatisticalClock.CLOCK(), Simulator.currentState());
		log.takeStateIn(currentState);
	}

	public void start() {
		signals();
	}
}
