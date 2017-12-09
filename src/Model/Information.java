package Model;

public class Information {

	private double averageTimeInOnePeriod;
	private double observationTime;

	public Information(double average, double time) {
		this.averageTimeInOnePeriod = average;
		this.observationTime = time;
	}
	
	public double getAverageTime() {
		return this.averageTimeInOnePeriod;
	}
	
	public double getTime() {
		return this.observationTime;
	}
	

}
