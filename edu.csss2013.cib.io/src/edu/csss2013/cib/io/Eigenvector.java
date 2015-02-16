package edu.csss2013.cib.io;

import java.util.Iterator;

import edu.csss2013.cib.IScenarioLink;
import edu.csss2013.cib.IScenarioNetwork;
import edu.csss2013.cib.IScenarioNode;

public class Eigenvector {

	private double accuracy = 1E-20;
	private int maxIter = 10000;
	private boolean weighted = true;

	public double[] calculate(IScenarioNetwork network){
		double[] centralites = new double[network.getNodeCount()];
		double[] newCentralities = new double[network.getNodeCount()];
		

		double v = 1./(double)network.getNodeCount();
		for(int i=0;i<network.getNodeCount();i++){
			centralites[i] = 1;
		}
		double err = 1.;
		IScenarioNode node;
		double score,scoreSum;
		int itCount =0;
		double change = Double.MAX_VALUE;
		while(change>1E-20 && itCount<maxIter){
			Iterator<? extends IScenarioNode> it = network.getNodes();
			scoreSum=0.;
			double max = 0;
			
			while(it.hasNext()){
				node=it.next();
				
				newCentralities[node.getIndex()] =0; //Gephi implementation would not reset this value
				for(IScenarioLink link : node.getIncoming()){
					if(weighted) {
						newCentralities[node.getIndex()]+=link.getWeight()*centralites[link.getStartNode().getIndex()];
					}
					else {
						newCentralities[node.getIndex()]+=centralites[link.getStartNode().getIndex()];
					}					     
				}				
				scoreSum+=newCentralities[node.getIndex()];
				max = Math.max(max, newCentralities[node.getIndex()]);
				
			}
			
			err = 0.;
			/*double eMin = Double.MAX_VALUE,eMax=0.,e=0,iMin =Double.MAX_VALUE,iMax = 0;
			for(int i=0;i<centralites.length;i++){
				err+=Math.abs(centralites[i]-newCentralities[i]);
				e = Math.abs(centralites[i]-newCentralities[i])/centralites[i];
				if(e<eMin) eMin = e;
				if(e>eMax) eMax = e;
				if(newCentralities[i]<iMin) iMin = newCentralities[i];
				if(newCentralities[i]>iMax) iMax = newCentralities[i];
			}*/
			//System.out.println("Error "+err+" "+eMin+" "+eMax+" "+iMin+" "+iMax+" "+entropy);
			//hold = centralites;
			change = 0;
			for(int i=0;i<centralites.length;i++){
				if(max!=0){
					change+=Math.abs(centralites[i] - newCentralities[i]/max);
					centralites[i] = newCentralities[i]/max;
				}
			}
			//System.out.println(change);
			//newCentralities = hold;
			itCount++;
		}
		//System.out.println(itCount);
		//System.out.println(change);
		/*for(double d:newMatrix){
			System.out.println(d);
		}*/
		return centralites;

	}

	public static void main(String[] args) {

	}

}
