package edu.csss2013.cib.impl.solver;

import java.util.ArrayList;
import java.util.List;

import edu.csss2013.cib.impl.ScenarioLinkHint;

public class BoltzmannSolver extends AbstractStochasticAdiabaticSolver {

	private double beta = 1;
	private double balanceSum;	
	private List<ScenarioLinkHint> hints = new ArrayList<ScenarioLinkHint>();	
	//private IBalance balance;
		
	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}
	
	public double calculateValue(double impactGradient){
		return Math.exp(beta*(impactGradient));
	}
	

}
