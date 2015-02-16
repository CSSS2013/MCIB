package edu.csss2013.cib.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.csss2013.cib.ICibMatrix;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IScenarioLink;
import edu.csss2013.cib.IScenarioNetwork;
import edu.csss2013.cib.IScenarioNode;
import edu.csss2013.cib.impl.Balance;
import edu.csss2013.cib.impl.BalanceCalculator;
import edu.csss2013.cib.impl.ScenarioNetwork;
import edu.csss2013.cib.impl.ScenarioTreeNode;

public class SimpleCSVWriter {
	
	private BufferedWriter nodeWriter;
	private BufferedWriter linkWriter;
	private Eigenvector ev = new Eigenvector();
	
	public void write(IScenarioNetwork network,ICibMatrix matrix,String suffix,File folder) throws IOException{
		nodeWriter  = new BufferedWriter(new FileWriter(new File(folder,"\\nodes"+suffix+".dat")));
		
		String line =  "Id";
		for(IDescriptor<?> d: matrix.getFactors()){
			line+=","+d.getName();

		}
		line+=",Consistency,SelfLink,EigenvectorCentrality";
		nodeWriter.write(line);
		nodeWriter.newLine();
		linkWriter = new BufferedWriter(new FileWriter(new File(folder,"\\links"+suffix+".dat")));
		linkWriter.write("Source,Target,Weight");
		linkWriter.newLine();
		Iterator<? extends IScenarioNode> it = network.getNodes();
		double selfLink = 0.;
		
		double[] eigenvector= ev.calculate(network);
		double sum = 0;
		
		IScenarioNode node;
		while(it.hasNext()){
			node = it.next();
			line =  node.getIndex()+"";
			for(IScenarioLink l:node.getOutgoing()){
				if(l.getEndNode().equals(node)){
					selfLink = l.getWeight();
				}
			}
			for(IDescriptor<?> d: node.getScenario().getFactors()){
				line+=","+node.getScenario().getDescriptor(d).getChoiceNumber();
			}
			BalanceCalculator bc = new BalanceCalculator();
			Balance b = bc.calculate(node.getScenario(), matrix);
			double con=0,conSum=0;
			for(IDescriptor<?> d : b.getScenario().getFactors()){
				con+=b.getScore(d);
				conSum+=b.getBestScore(d);
			}
			con /=conSum;
			
			line+=","+con+","+selfLink+","+eigenvector[node.getIndex()];
			nodeWriter.write(line);
			nodeWriter.newLine();
			for(IScenarioLink link:node.getIncoming()){
				if(link.getStartNode().getIndex()!=-1){
					linkWriter.write(link.getStartNode().getIndex()+","+link.getEndNode().getIndex()+","+link.getWeight());
					linkWriter.newLine();
				}
			}
		}
		nodeWriter.close();
		linkWriter.close();
		
		double entropy = 0;
		double entropyProduction = 0;
		for(int i=0;i<eigenvector.length;i++){
			sum+=eigenvector[i];
		}
		for(int i=0;i<eigenvector.length;i++){
			eigenvector[i]/=sum;
		}
		it = network.getNodes();
		double e,score;
		while(it.hasNext()){
			node = it.next();
			e = eigenvector[node.getIndex()];
			if(e!=0){
				entropy-=e*Math.log(e);
				score = 0;
				for(IScenarioLink link:node.getOutgoing()){
					score-=link.getWeight()*Math.log(link.getWeight());
				}
				entropyProduction+=e*score;
			}
		}
		double relEntropy = entropy/Math.log(network.getNodeCount());
		
		double stateCount = 1; //selflink
		for(IDescriptor<?> d:matrix.getFactors()){
			stateCount+=d.getStateCount();
		}
		
		stateCount-=matrix.getFactors().size();
		
		double relEntropyProduction = entropyProduction/Math.log(stateCount);
		
		System.out.println(entropy+","+relEntropy+","+entropyProduction+","+relEntropyProduction);
	}
	
	public void writeFilter(ScenarioNetwork network,ICibMatrix matrix, int nNodes) throws IOException{
		
		List<ScenarioTreeNode> sorted = new ArrayList<ScenarioTreeNode>();
		Iterator<IScenarioNode> it = network.getNodes();
		while(it.hasNext()){
			sorted.add((ScenarioTreeNode) it.next());			
		}
		Collections.sort(sorted, new Comparator<ScenarioTreeNode>() {

			@Override
			public int compare(ScenarioTreeNode o1, ScenarioTreeNode o2) {
				return Double.compare(o2.getDegree(),o1.getDegree()); // want descending order
			}
		});

		for(int i=sorted.size()-1;i>=nNodes ;i--){
			sorted.get(i).setIndex(-1);
			sorted.remove(i);
		}
		
		
		nodeWriter  = new BufferedWriter(new FileWriter(new File("nodes.dat")));

		String line =  "Id";
		for(IDescriptor<?> d: matrix.getFactors()){
			line+=","+d.getName();

		}	
		nodeWriter.write(line);
		nodeWriter.newLine();
		linkWriter = new BufferedWriter(new FileWriter(new File("links.dat")));
		linkWriter.write("Source,Target,Weight");
		linkWriter.newLine();
		for(ScenarioTreeNode node:sorted){
			line =  node.getIndex()+"";
			for(IDescriptor<?> d: node.getScenario().getFactors()){
				line+=","+node.getScenario().getDescriptor(d).getChoiceNumber();
			}			
			nodeWriter.write(line);
			nodeWriter.newLine();
			for(IScenarioLink link:node.getIncoming()){
				if(link.getStartNode().getIndex()!=-1){
					linkWriter.write(link.getStartNode().getIndex()+","+link.getEndNode().getIndex()+","+link.getWeight());
					linkWriter.newLine();
				}
			}
		}
		nodeWriter.close();
		linkWriter.close();
	}

}
