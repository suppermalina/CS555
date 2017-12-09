/**
 * 
 */
package ContainersInstance;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Timer;

import EventsInstances.Customer;
import ExecutionalInstances.Controller;
import Model.Containers;
import Model.Information;
import Model.InformationCollector;
import Model.Task;

/**
 * @author mali
 *
 */
public class ModelSystem extends Containers {
	private Servers server;
	private Queueing queue;
	private static InformationCollector collector;
	private Task task;
	private Deque<Deque<Information>> pitch;
	private Timer timer;
	private double samplingPeriod;
	
	public ModelSystem() {
		this.samplingPeriod = Controller.period;
		this.timer = new Timer();
		this.server = Servers.getInstance();
		this.queue = Queueing.getInstance();
		collector = new InformationCollector();
		collector.setPeriod(samplingPeriod);
		this.pitch = new LinkedList<Deque<Information>>();
	}
	
	public void setTimeTask() {
		timer.schedule(collector, (long)samplingPeriod, (long)samplingPeriod);
	}
	
	/* (non-Javadoc)
	 * @see Model.Containers#takeTaskIn(Model.Task)
	 */
	
	public synchronized void generateNewCustomer(Task t) {
		if (t != null) {
			Customer newCust = new Customer();
			Controller.reporter.generatingLog(newCust.toString() + " was generated at: " + Controller.clock.CLOCK()
					+ ", involked by " + t.getTyp() + t.getId());
			takeTaskIn(newCust);
		}

	}
	
	@Override
	public boolean takeTaskIn(Task e) {
		if(queue.takeTaskIn(e) == true) {
			return true;
		} else {
			collector.takeRejcetedInformation(e);
			return false;
		}
		
	}
	
	public static boolean customersOutFromServer(Task t) {
		collector.takeServiceInformation(t);
		return true;
	}
	
	public int currentState() {
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

	public void modify() {
		server.servers[0].modify();
		server.servers[1].modify();
	}
	
	public Deque<Deque<Information>> pitcher() {
		pitch.offerLast(collector.getSystemInform());
		pitch.offerLast(collector.getQueueInform());
		pitch.offerLast(collector.getServerInform());
		pitch.offerLast(collector.getRejectedInform());
		if(pitch == null) {
			System.out.println("PITCHER IS DAMN NULL");
			System.exit(-1);
		}
		return pitch;
	}

	/* (non-Javadoc)
	 * @see Model.Containers#popTaskOut()
	 */
	@Override
	public Task popTaskOut() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see Model.Containers#getSize()
	 */
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
