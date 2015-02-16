package edu.csss2013.cib.impl.solver;

import edu.csss2013.cib.IBalance;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.impl.AbstractSolver;
import edu.csss2013.cib.impl.Scenario;
import edu.csss2013.cib.impl.ScenarioLinkHint;

public class UpdateRandomSolver extends AbstractSolver {

    @Override
	protected ScenarioLinkHint[] calculateUpdatedScenarios(IBalance b){
		//System.out.println(b.isConsistent());                
        double check=0;
        double weight= 1 / ((double)(b.getScenario().getFactors().size()));
		if(true){ //Some consistan scenareo is being marked as inconsistent, hence error later.			
			IState<?>[] d = new IState[b.getScenario().getFactors().size()];		
                        ScenarioLinkHint[] returnMe = new ScenarioLinkHint[b.getScenario().getFactors().size()];
			int iii=0;
			int counter = 0;
			for(IDescriptor<?> f:b.getScenario().getFactors()){				
                                  iii=0;
                                  
                            for(IDescriptor<?> g:b.getScenario().getFactors()){
                                  System.out.println(" "+g.getName().equalsIgnoreCase(f.getName()) + " best:"+ b.getBestDescriptor(g).toString() + " Current:"+  b.getDescriptor(g));
                      		d[iii] = (g.getName().equalsIgnoreCase(f.getName())?b.getBestDescriptor(g) : b.getDescriptor(g) ) ;                           		                 
                                 iii++;            
                            }                            
                            Scenario s = new Scenario(b.getScenario().getFactors());                            
					s.setDescriptor(d);					                                                    
                            returnMe[counter]= new ScenarioLinkHint(b.getScenario(),s,weight);  
                            counter++;
                            check+=weight;
                        }
                        System.out.print(check+" ");
			return returnMe;
		}
		return null; // no new scenario required ==> "consistent" scenario
	}

	

}
