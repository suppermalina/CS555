package Model;

import ExecutionalInstances.StatisticalClock;

public class Test {
	private synchronized void sleeper() {
		try {
			this.wait(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		System.out.println(StatisticalClock.CLOCK());
		new Test().sleeper();
		System.out.println(StatisticalClock.CLOCK());
	}

}
