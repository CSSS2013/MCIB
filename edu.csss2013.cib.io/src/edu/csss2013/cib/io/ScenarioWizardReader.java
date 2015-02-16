package edu.csss2013.cib.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.csss2013.cib.ICibMatrix;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.impl.CibMatrix;
import edu.csss2013.cib.impl.DescriptorCollection;

public class ScenarioWizardReader {

	public ICibMatrix read(File file) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));

		String line;
		String[] lineSplit;
		reader.readLine();
		reader.readLine();
		DescriptorCollection descriptors = new DescriptorCollection();
		IDescriptor currentDesc=null;
		while((line=reader.readLine())!=null){
			if(line.startsWith("#")){
				break;
			}
			else if(line.startsWith("&")){
				currentDesc = descriptors.makeFactor(line.substring(1));
			}
			else{
				line = line.trim();				
				descriptors.makeDescriptor(currentDesc, line.substring(1));
			}
		}
		while(!(line=reader.readLine()).startsWith("#")){

		}
		while(!(line=reader.readLine()).startsWith("#")){

		}
		while(!(line=reader.readLine()).startsWith("#")){

		}
		while(!(line=reader.readLine()).startsWith("#")){

		}

		
		CibMatrix matrix = new CibMatrix(descriptors);
		for(IDescriptor d:descriptors){
			for(int i = 0;i<d.getStateCount();i++){
				line=reader.readLine();				
				lineSplit = line.split(",");
				int count = 0;
				for(IDescriptor d2:descriptors){
					for(int j = 0;j<d2.getStateCount();j++){
						int weight = Integer.valueOf(lineSplit[count].trim());
						if(weight!=0){
							matrix.setImpact(d.getState(i), d2.getState(j), weight);
						}
						count++;
					}
				}
			}
		}


		reader.close();
		return matrix;
	}

	public static void main(String[] args) throws IOException {
		File file = new File("D:\\STUDENTEN\\LIANG\\Daten\\Gesamt_12.scw");
		ScenarioWizardReader s = new ScenarioWizardReader();
		s.read(file);
	}

}
