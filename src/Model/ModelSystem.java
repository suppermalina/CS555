/**
 * 
 */
package Model;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Timer;

/**
 * @author mali
 *
 */
class ModelSystem extends Containers {
	private Servers server;
	private Queueing queue;
	private static InformationCollector collector;
	private Deque<Deque<Information>> pitch;
	private Timer timer;
	private double samplingPeriod;

	ModelSystem() {
		this.samplingPeriod = Controller.period;
		this.timer = new Timer();
		this.server = Servers.getInstance();
		this.queue = Queueing.getInstance();
		collector = new InformationCollector();
		collector.setPeriod(samplingPeriod);
		this.pitch = new LinkedList<Deque<Information>>();
	}

	protected void setTimeTask() {
		timer.schedule(collector, (long) samplingPeriod, (long) samplingPeriod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */

	protected synchronized void generateNewCustomer(Task t) {
		if (t != null) {
			Customer newCust = new Customer();
			takeTaskIn(newCust);
		}

	}

	protected void cleanUnexecutedTasks() {
		server.cleanUnexecutedTasks();
		queue.cleanUnexecutedTasks();
	}

	@Override
	protected boolean takeTaskIn(Task e) {
		if (queue.takeTaskIn(e) == true) {
			return true;
		} else {
			collector.takeRejcetedInformation(e);
			return false;
		}

	}

	protected static boolean customersOutFromServer(Task t) {
		if (t != null) {
			collector.takeServiceInformation(t);
		}
		return true;
	}

	protected int currentState() {
		return queue.getSize() + server.getSize();
	}

	protected boolean specialSituation_SystemFull() {
		for (int i = 0; i < 2; i++) {
			server.servers[i].takeTaskIn(new Customer());
		}
		for (int i = 0; i < queue.capacity; i++) {
			Customer temp = new Customer();
			queue.takeTaskIn(temp);
		}
		return true;
	}

	protected boolean specialSituation_PartialFull(int t) {
		if (t >= 2) {
			for (int i = 0; i < 2; i++) {
				server.servers[i].takeTaskIn(new Customer());
			}
		}
		for (int i = 0; i < t - 2; i++) {
			queue.takeTaskIn(new Customer());
		}
		return true;
	}

	protected void modify() {
		server.servers[0].modify();
		server.servers[1].modify();
	}

	protected Deque<Deque<Information>> pitcher() {
		pitch.offerLast(collector.getSystemInform());
		pitch.offerLast(collector.getQueueInform());
		pitch.offerLast(collector.getServerInform());
		pitch.offerLast(collector.getServicedQuantityInform());
		pitch.offerLast(collector.getRejectedQuantityInform());
		pitch.offerLast(collector.getServicedRatioInform());
		pitch.offerLast(collector.getRejectedRatioInform());
		pitch.offerLast(collector.getFinalStatisticData());
		if (pitch == null) {
			System.out.println("PITCHER IS DAMN NULL");
			System.exit(-1);
		}
		return pitch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public Task popTaskOut() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Containers#getSize()
	 */
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
