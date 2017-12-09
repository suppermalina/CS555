import java.util.TimerTask;

public class MyCollector {
	private TimerTask task;
	private long samplingPeriod;
	private long simulationPeriod;
	
	public void schedule(TimerTask task, long samplingPeriod, long simulationPeriod) {
		this.task = task;
		this.samplingPeriod = samplingPeriod;
		this.simulationPeriod = simulationPeriod;
		int runs = (int) (this.simulationPeriod / this.samplingPeriod);
		while (runs > 0) {
			try {
				task.wait(samplingPeriod);
				task.run();
			} catch(Exception e) {
				e.printStackTrace();
			}
			runs--;
		}
	}
}
