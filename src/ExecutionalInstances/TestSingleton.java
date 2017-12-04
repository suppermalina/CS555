package ExecutionalInstances;

public class TestSingleton implements Runnable {
	public static void main(String[] args) {
		System.out.println("Start at: " + StatisticalClock.CLOCK());
		new TestSingleton().run();
		System.out.println("End at: " + StatisticalClock.CLOCK());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long time = (long) (RandomNumberGenerator.getInstance(0.5) * 1000);
		try {
			System.out.println("Waiting: " + time);
			wait(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
