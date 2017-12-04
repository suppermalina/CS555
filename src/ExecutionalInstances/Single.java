package ExecutionalInstances;

public class Single {
	private static Single instance = null;
	private Single() {
		System.out.println("hello world");
	}
	
	public static synchronized Single getInstance() {
		if (instance == null) {
			instance = new Single();
		}
		return instance;
	}
	

}
