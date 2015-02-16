package edu.csss2013.cib.impl.solver;


public class ArctanSolver2 extends AbstractStochasticAdiabaticSolver {

	private double beta = 1;
	

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}	
	

	@Override
	public double calculateValue(double impactGradient) {
		return Math.atan(beta*impactGradient)+ Math.PI/2;
	}

	



}
