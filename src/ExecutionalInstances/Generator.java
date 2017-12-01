/**
 * 
 */
package ExecutionalInstances;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import ContainersInstance.Queue;
import ContainersInstance.Server;
import ContainersInstance.StateList;
import ContainersInstance.StatisticalCounter;
import ContainersInstance.TaskList;
import Model.BaseGenerator;
import Model.Containers;
import Model.Event;

/**
 * @author mali
 *
 */
public class Generator implements BaseGenerator {
	
	/* (non-Javadoc)
	 * @see Model.BaseGenerator#getEvent(int, java.lang.String)
	 */
	@Override
	public Event getEvent(String type, int ID) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see Model.BaseGenerator#getContainer(int, java.lang.String)
	 */
	@Override
	public Containers getContainer(String type, int ID) {
		// TODO Auto-generated method stub
		if (type.equalsIgnoreCase("server")) {
			return new Server(type, ID);
		} else if (type.equalsIgnoreCase("queue")) {
			return new Queue(type, ID);
		} else if (type.equalsIgnoreCase("eventlist")) {
			return new TaskList(type, ID);
		} else if (type.equalsIgnoreCase("statelist")) {
			return new StateList(type, ID);
		} else if (type.equals("counter")) {
			return new StatisticalCounter(type, ID);
		} else {
			System.exit(-1);
			new Exception("Illegal type!!!");
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see Model.BaseGenerator#getInterval(long)
	 */
	@Override
	public double getInterval(long rate) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see Model.BaseGenerator#getRandom()
	 */
	@Override
	public double getRandom() {
		// TODO Auto-generated method stub
		return 0;
	}

}
