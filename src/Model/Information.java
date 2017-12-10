package Model;

class Information {

	private double averageTimeInOnePeriod;
	private double observationTime;

	protected Information(double average, double time) {
		this.averageTimeInOnePeriod = average;
		this.observationTime = time;
	}
	
	protected double getAverageTime() {
		return this.averageTimeInOnePeriod;
	}
	
	protected double getTime() {
		return this.observationTime;
	}
	

}
