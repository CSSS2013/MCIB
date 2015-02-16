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

public class BoltzmannSolver2 extends AbstractTreeSolver {

	private double beta = 1;
	private double balanceSum;	
	private List<ScenarioLinkHint> hints = new ArrayList<ScenarioLinkHint>();	
	//private IBalance balance;
	private BalanceCalculator calc = new BalanceCalculator();

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}


	protected ScenarioLinkHint[] calculateUpdatedScenariosOld(IScenarioNode scenario,ICibMatrix matrix){
		
		Balance balance = 
				calc.calculate(scenario.getScenario(), matrix);
		balanceSum = 0;
		hints.clear();
		for(IDescriptor<?> f:balance.getScenario().getFactors()){
			for (int d=0; d<f.getStateCount(); d++){
				balanceSum+=Math.exp(beta*(balance.getValue(f.getState(d)) ));				
			}  //go through and sum over ALL descriptors to find all possible exists.                          
		}

		
		
		
		
		for(IDescriptor<?> f:balance.getScenario().getFactors()){
			IState<?>[] dArray = new IState[balance.getScenario().getFactors().size()];
			int iii=0;
			for(IDescriptor<?> g:balance.getScenario().getFactors()){
				dArray[iii]= balance.getDescriptor(g);
				iii++;
			}
			
			for (int d=0; d<f.getStateCount(); d++){
				dArray[balance.getScenario().getFactors().indexOf(f)]=f.getState(d);

				double thisWeight=Math.exp(beta*(balance.getValue(f.getState(d)) ));
				
				Scenario s = new Scenario(balance.getScenario().getFactors());
				s.setDescriptor(dArray);
				hints.add(new ScenarioLinkHint(balance.getScenario(),s,thisWeight/balanceSum) );                               
				
			}  //And now make all the links...
		}

		return hints.toArray(new ScenarioLinkHint[hints.size()]);
		//}
		//return null; // no new scenario required ==> "consistent" scenario
	}

	protected ScenarioLinkHint[] calculateUpdatedScenarios(IScenarioNode scenario,ICibMatrix matrix){
		
		Balance balance = 
				calc.calculate(scenario.getScenario(), matrix);
		balanceSum = calculateValue(0, 0);
		hints.clear();

		for(IDescriptor<?> f:balance.getScenario().getFactors()){
			for (int d=0; d<f.getStateCount(); d++){
				if(!f.getState(d).equals(balance.getScenario().getDescriptor(f))){
					//balanceSum+=Math.exp(beta*(balance.getValue(f.getState(d))-balance.getScore(f)));
					balanceSum+=calculateValue(balance.getScore(f),balance.getValue(f.getState(d)));
				}
			}                            
		}



		/*IState<?>[] dArray = new IState[balance.getScenario().getFactors().size()];		
		int indexOfF;
		int iii=0;
		for(IDescriptor<?> g:balance.getScenario().getFactors()){
			dArray[iii]= balance.getDescriptor(g);
			iii++;
		}*/

		// Add self link
		hints.add(new ScenarioLinkHint(scenario.getScenario(),scenario.getScenario(),1./balanceSum));



		IState<?> currentState;
		for(IDescriptor<?> f:balance.getScenario().getFactors()){	
			IState<?>[] dArray = new IState[balance.getScenario().getFactors().size()];		
			int indexOfF;
			int iii=0;
			for(IDescriptor<?> g:balance.getScenario().getFactors()){
				dArray[iii]= balance.getDescriptor(g);
				iii++;
			}
			indexOfF = balance.getScenario().getFactors().indexOf(f);
			currentState = dArray[indexOfF];

			for (int d=0; d<f.getStateCount(); d++){
				if(!f.getState(d).equals(balance.getScenario().getDescriptor(f))){
					double thisWeight=Math.exp(beta*(balance.getScore(f))-balance.getValue(f.getState(d)));					
					Scenario s = new Scenario(balance.getScenario().getFactors());
					dArray[indexOfF]=f.getState(d);
					s.setDescriptor(dArray);
					hints.add(new ScenarioLinkHint(balance.getScenario(),s,thisWeight/balanceSum) );           
				}
			}  
			dArray[indexOfF] = currentState;
			//And now make all the links...
		}
		return hints.toArray(new ScenarioLinkHint[hints.size()]);
	}
	
	private double calculateValue(double chosenValue, double newValue){
		return Math.exp(beta*(newValue-chosenValue));
	}

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
