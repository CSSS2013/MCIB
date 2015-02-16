package edu.csss2013.cib.impl;

import edu.csss2013.cib.ICibMatrix;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IScenario;
import edu.csss2013.cib.IState;

public class BalanceCalculator {
	
	private boolean includeDiagonal=false;
	private Balance b = new Balance();
	
	public Balance calculate(IScenario scenario,ICibMatrix matrix){
		b.setScenario(scenario);
		IState<?> variableState;
		double value = 0;
		for(IDescriptor<?> f:matrix.getFactors()){	
			IState<?> chosenState = scenario.getDescriptor(f);
			for(int i=0;i<f.getStateCount();i++){				
				variableState = f.getState(i);
				value = 0;
				for(IDescriptor<?> f2:matrix.getFactors()){
					if(!f.equals(f2) || includeDiagonal){						
						value +=matrix.getImpact(scenario.getDescriptor(f2),variableState);
					}
				}	
				
				if(chosenState.equals(variableState)){					
					b.setChosenDescriptor(variableState, value);
				}
				else{					
					b.setNonChosenDescriptor(variableState, value);
				}
			}
		}
		
		return b;
	}
	
	/*public Balance calculate(IScenario scenario,ICibMatrix matrix){
		Balance b = new Balance(scenario);
		IState<?> variableState;
		double value = 0;
		
		
		
		for(IState<?> chosenState:scenario){	
			
			for(int i=0;i<chosenState.getFactor().getStateCount();i++){				
				variableState = chosenState.getFactor().getState(i);
				value = 0;
				for(IDescriptor<?> f2:matrix.getFactors()){
					if(!variableState.getFactor().equals(f2) || includeDiagonal){						
						value +=matrix.getImpact(scenario.getDescriptor(f2),variableState);
					}
				}	
				
				if(chosenState.equals(variableState)){					
					b.setChosenDescriptor(variableState, value);
				}
				else{					
					b.setNonChosenDescriptor(variableState, value);
				}
			}
		}
		return b;
	}*/

}
