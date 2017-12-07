package Model;

import ExecutionalInstances.StatisticalClock;

public class Test {
	private synchronized void sleeper() {
		try {
			StatisticalClock clock = new StatisticalClock();
			System.out.println(clock.NANOCLOCK());
			this.wait(5);
			System.out.println(clock.NANOCLOCK());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Test().sleeper();
		}
		
		
	}

}
