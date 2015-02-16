package edu.csss2013.cib.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import edu.csss2013.cib.ICibMatrix;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IDescriptors;

public class TableReader {

	private int factorId=0;
	private int descriptorId=0;

	public void read(File file, ICibMatrix matrix) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		factorId=0;
		descriptorId=0;
		String line;
		String[] lineSplit;
		for(IDescriptor<?> f:matrix.getFactors()){
			for(int i=0;i<f.getStateCount();i++){
				IState<?> d1 = f.getState(i);
				line = reader.readLine();
				lineSplit = line.split(",");
				for(String value:lineSplit){
					IState<?> d2 = next(matrix.getFactors());
					matrix.setImpact(d1, d2, Double.valueOf(value));					
				}				
			}
		}		
	}

	private IState<?> next(IDescriptors collection){
		IDescriptor<?> f = collection.getDescriptor(factorId);
		IState<?> d = f.getState(descriptorId);

		if(descriptorId==f.getStateCount()-1){
			if((factorId==collection.size()-1)){
				factorId=0;
				descriptorId=0;
			}
			else{
				factorId++;			
				descriptorId=0;
			}
		}
		else{
			descriptorId++;
		} return d;
	}



}
