package edu.csss2013.cib.impl.solver;


public class LogisticSolver2 extends AbstractStochasticAdiabaticSolver {

	private double beta = 1;
    private double shift=1.0;
		
	public double getShift() {
		return shift;
	}

	public void setShift(double shift) {
		this.shift = shift;
	}	

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	
	
	public double calculateValue(double impactGradient){		
		return Math.exp(beta*(impactGradient+shift))/(1+Math.exp(beta*(impactGradient+shift)));
	}
	

}
