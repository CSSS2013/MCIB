package edu.csss2013.cib.io;

public class Interval implements Comparable<Interval>{
	
	private double startTime,endTime;
	
	public Interval(double start,double endTime){
		this.startTime = start;
		this.endTime = endTime;
	}

	public double getStartTime() {
		return startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	@Override
	public int compareTo(Interval o) {
		int c = Double.compare(getStartTime(), o.getStartTime());
		if(c==0.){
			c = Double.compare(getEndTime(), o.getEndTime());
		}
		return c;
	}

	
	

}
