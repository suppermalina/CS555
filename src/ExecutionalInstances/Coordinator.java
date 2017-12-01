/**
 * 
 */
package ExecutionalInstances;

import java.util.Observable;
import java.util.Observer;

/**
 * @author mali
 *
 */
public final class Coordinator implements Observer {
	private static volatile Coordinator instance = null;

    private Coordinator() {}

    public static Coordinator getInstance() {
        if (instance == null) {
            synchronized(Coordinator.class) {
                if (instance == null) {
                    instance = new Coordinator();
                }
            }
        }
        return instance;
    }

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
