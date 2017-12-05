package TestQuatz;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class HelloWorld {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = (Logger) LogManager.getLogger(HelloWorld.class.getName());

	public static void main(String[] args) {
		org.apache.log4j.PropertyConfigurator.configure("/Users/mali/Documents/workspace/CS555/src/log4j.properties");
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		System.out.println("hello world");

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}
}
