package edu.csss2013.cib.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.csss2013.cib.IScenarioLink;
import edu.csss2013.cib.IScenarioNetwork;
import edu.csss2013.cib.IScenarioNode;

public class CSVWriter {
	
	private File nodeFile;
	private File edgeFile;
	private Map<IScenarioNode, Integer> map = new HashMap<IScenarioNode, Integer>();
	
	public CSVWriter(File nodeFile,File edgeFile){
		this.nodeFile=nodeFile;
		this.edgeFile=edgeFile;
	}
	
	public void write(IScenarioNetwork network) throws IOException{
		writeNodes(network);
		writeEdges(network);
	}
	
	private void writeNodes(IScenarioNetwork network) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(nodeFile));
		int counter = 0;
		writer.write("Id;Scenarios");
		writer.newLine();
		Iterator<? extends IScenarioNode> it = network.getNodes();
		while(it.hasNext()){
			IScenarioNode node = it.next();
			map.put(node, counter);
			writer.write(counter+";"+node.getScenario());
			writer.newLine();
			counter++;
		}
		writer.close();
	}
	
	private void writeEdges(IScenarioNetwork network) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(edgeFile));	
		writer.write("Source;Target;Weight");
		writer.newLine();
		Iterator<? extends IScenarioNode> it = network.getNodes();
		while(it.hasNext()){
			IScenarioNode node = it.next();			
			for(IScenarioLink link:node.getOutgoing()){
				writer.write(map.get(link.getStartNode())+";"+map.get(link.getEndNode())+";"+link.getWeight());
				writer.newLine();
			}
		}
		writer.close();
	}
	
	public void writeNodes(IScenarioNetwork network,String timeInterval) throws IOException{
		map = new HashMap<IScenarioNode, Integer>();
		BufferedWriter writer = new BufferedWriter(new FileWriter(nodeFile));
		int counter = 0;
		writer.write("Id;Scenarios;Time Interval");
		writer.newLine();
		Iterator<? extends IScenarioNode> it = network.getNodes();
		while(it.hasNext()){
			IScenarioNode node = it.next();
			map.put(node, counter);
			writer.write(counter+";"+node.getScenario()+";<"+timeInterval+">");
			writer.newLine();
			counter++;
		}
		writer.close();
	}
	
	public void writeEdges(IScenarioNetwork network,String timeInterval) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(edgeFile));	
		writer.write("Source;Target;Weight;Time Interval");
		writer.newLine();
		Iterator<? extends IScenarioNode> it = network.getNodes();
		while(it.hasNext()){
			IScenarioNode node = it.next();
			for(IScenarioLink link:node.getOutgoing()){
				writer.write(map.get(link.getStartNode())+";"+map.get(link.getEndNode())+";"+
						link.getWeight()+";<"+timeInterval+">");
				writer.newLine();
			}
		}
		writer.close();
	}
	
	

}
