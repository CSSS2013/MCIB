package edu.csss2013.cib.impl.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.csss2013.cib.IBalance;
import edu.csss2013.cib.ICibMatrix;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IScenario;
import edu.csss2013.cib.IScenarioNode;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.impl.AbstractTreeSolver;
import edu.csss2013.cib.impl.Balance;
import edu.csss2013.cib.impl.BalanceCalculator;
import edu.csss2013.cib.impl.Scenario;
import edu.csss2013.cib.impl.ScenarioLinkHint;

public abstract class AbstractStochasticAdiabaticSolver extends AbstractTreeSolver {

	private double beta = 1;
	private double balanceSum;	
	private List<ScenarioLinkHint> hints = new ArrayList<ScenarioLinkHint>();	
	//private IBalance balance;
	private BalanceCalculator calc = new BalanceCalculator();
	private List<Double> valueList = new ArrayList<>();
	private List<IScenario> scenarioList = new ArrayList<>();

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}


	

	protected ScenarioLinkHint[] calculateUpdatedScenarios(IScenarioNode scenario,ICibMatrix matrix){
		
		Balance balance = 
				calc.calculate(scenario.getScenario(), matrix);
		
		hints.clear();
		valueList.clear();
		scenarioList.clear();		
		
		IState<?> currentState;
		IState<?>[] dArray = new IState[balance.getScenario().getFactors().size()];		
		int indexOfF;
		int iii=0;
		
		double weight = calculateValue(0); // Self-Link
		balanceSum+=weight;
		for(IDescriptor<?> g:balance.getScenario().getFactors()){
			dArray[iii]= balance.getDescriptor(g);
			iii++;
		}
		
		for(IDescriptor<?> f:balance.getScenario().getFactors()){
			indexOfF = balance.getScenario().getFactors().indexOf(f);
			currentState = dArray[indexOfF];
			for (int d=0; d<f.getStateCount(); d++){
				if(!f.getState(d).equals(balance.getScenario().getDescriptor(f))){
					weight = calculateValue(balance.getValue(f.getState(d))-balance.getScore(f));
					Scenario s = new Scenario(balance.getScenario().getFactors());
					dArray[indexOfF]=f.getState(d);
					s.setDescriptor(dArray);
					valueList.add(weight);
					scenarioList.add(s);
					balanceSum+=weight;
				}
			}  
			dArray[indexOfF] = currentState;
		}		
		// Add self link
		hints.add(new ScenarioLinkHint(scenario.getScenario(),scenario.getScenario(),1./balanceSum));
		
		// add links to others
		for(int i=0;i<valueList.size();i++){
			hints.add(new ScenarioLinkHint(balance.getScenario(),scenarioList.get(i),valueList.get(i)/balanceSum) );
		}	
		
		return hints.toArray(new ScenarioLinkHint[hints.size()]);
	}
	
	public abstract double calculateValue(double impactGradient);
	/*{
		return Math.exp(beta*(newValue-chosenValue));
	}*/

	public void calculate(IScenario scenario,ICibMatrix matrix){
		Balance balance = 
				calc.calculate(scenario, matrix);
		balanceSum = 1;
		hints.clear();

		for(IDescriptor<?> f:balance.getScenario().getFactors()){
			for (int d=0; d<f.getStateCount(); d++){
				if(!f.getState(d).equals(balance.getScenario().getDescriptor(f))){
					balanceSum+=Math.exp(beta*(balance.getValue(f.getState(d))-balance.getScore(f)));
				}
			}  //go through and sum over ALL descriptors to find all possible exists.                          
		}

		int counterer=1;

		IState<?>[] dArray = new IState[balance.getScenario().getFactors().size()];		
		int indexOfF;
		int iii=0;
		for(IDescriptor<?> g:balance.getScenario().getFactors()){
			dArray[iii]= balance.getDescriptor(g);
			iii++;
		}

		hints.add(0	, new ScenarioLinkHint(scenario,scenario,1./balanceSum));
		System.out.println(1./balanceSum+" "+Arrays.toString(scenario.getChoices()));
		


		IState<?> currentState;
		for(IDescriptor<?> f:balance.getScenario().getFactors()){			  
			indexOfF = balance.getScenario().getFactors().indexOf(f);
			currentState = dArray[indexOfF];


			for (int d=0; d<f.getStateCount(); d++){
				if(!f.getState(d).equals(currentState)){
					System.out.println(f.getState(d) +" "+balance.getValue(f.getState(d)));
					System.out.println(currentState+" "+balance.getScore(f));
					System.out.println(balance.getValue(f.getState(d))-balance.getScore(f));
					double thisWeight=Math.exp(beta*(balance.getValue(f.getState(d))-balance.getScore(f)));
					Scenario s = new Scenario(balance.getScenario().getFactors());
					dArray[indexOfF]=f.getState(d);
					s.setDescriptor(dArray);

					hints.add(counterer, new ScenarioLinkHint(balance.getScenario(),s,thisWeight/balanceSum) );
					System.out.println(thisWeight/balanceSum+" "+Arrays.toString(s.getChoices()));
					counterer++;

				}			



			}  
			dArray[indexOfF] = currentState;
			//And now make all the links...
		}

	}

	/*@Override
	public void evaluate(IScenario scenario, FactorIterator iterator) {
		double energyLevel = 0;
		for(IFactor<?> f: scenario.getFactors()){
			energyLevel += balance.getValue(scenario.getDescriptor(f));
		}
		/**
	 * TODO check if the following is right
	 */
	/*	double probability = Math.exp(-beta*(balanceSum-energyLevel)); 
		this.hints.add(new ScenarioLinkHint(scenario, probability));
	}*/



}
