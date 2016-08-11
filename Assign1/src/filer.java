import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class filer {
		
	public static void printTable(double[][] result, String name){
		Writer writer = null;
		String date = new SimpleDateFormat(" (dd-MM-yyyy HH_mm_ss)").format(new Date());
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(name+date+".csv"), "utf-8"));
			
			writer.write("Frequency,UX,UY\r\n\r\n");
			
			for(int k=0; k<result.length; k++){
		    	writer.write(result[k][0]+","+result[k][1]+","+result[k][2]+"\r\n");
		    }
		} catch (IOException e) {
			warn();
		} finally {
			   try {writer.close();} catch (Exception ex) {warn();}
		} 
		
	}
	
	public static void printElectric(double[][] R, double[][] L, double[][] C, String[] V){
		Writer writer = null;
		String date = new SimpleDateFormat(" (dd-MM-yyyy HH_mm_ss)").format(new Date());
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("Electric Result"+date+".csv"), "utf-8"));
			
			writer.write("Resistance:\r\n\r\n");
			
			for(int i=0; i<R.length; i++){
				for(int j=0; j<R[0].length; j++){
					writer.write(R[i][j]+",");
				}
				writer.write("\r\n");
		    }
			
			writer.write("\r\n\r\nInductance:\r\n\r\n");
			
			for(int i=0; i<L.length; i++){
				for(int j=0; j<L[0].length; j++){
					writer.write(L[i][j]+",");
				}
				writer.write("\r\n");
		    }
			
			writer.write("\r\n\r\nCapacitance:\r\n\r\n");
			
			for(int i=0; i<C.length; i++){
				for(int j=0; j<C[0].length; j++){
					writer.write(C[i][j]+",");
				}
				writer.write("\r\n");
		    }
			
			writer.write("\r\n\r\nVoltage:\r\n\r\n");
			
			for(int i=0; i<V.length; i++){
				writer.write(V[i]+"\r\n");
		    }
			
		} catch (IOException e) {
			warn();
		} finally {
			   try {writer.close();} catch (Exception ex) {warn();}
		} 
	}
	
	public static void trussToFile(double[][] result, String [] term, double [] dis, double [] forces){
		Writer writer = null;
		String date = new SimpleDateFormat(" (dd-MM-yyyy HH_mm_ss)").format(new Date());
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("Truss Solution"+date+".csv"), "utf-8"));
			
			writer.write("Stress\r\n");
			writer.write("Node One, Node Two, Stess(Mpa), State\r\n");
			
			for(int k=0; k<result.length; k++){
		    	writer.write(result[k][0]+","+result[k][1]+","+result[k][2]+","+term[k]+"\r\n");
		    }
			
			writer.write("Displacments\r\n");
			writer.write("Node #,X(m),Y(m)\r\n");
			
			for(int k=0; k<dis.length; k+=2){
				int count = (k+2)/2;
		    	writer.write(count+","+dis[k]+","+dis[k+1]+"\r\n");
		    }
			
			writer.write("Reaction Forces\r\n");
			writer.write("Node #,X(N),Y(N)\r\n");
			
			for(int k=0; k<forces.length; k+=2){
				int count = (k+2)/2;
		    	writer.write(count+","+forces[k]+","+forces[k+1]+"\r\n");
		    }
			
		} catch (IOException e) {
			warn();
		} finally {
			   try {writer.close();} catch (Exception ex) {warn();}
		} 
	}
	
	public static void singleToFile(double[][] result, double timeStep, String name){
		Writer writer = null;
		String date = new SimpleDateFormat(" (dd-MM-yyyy HH_mm_ss)").format(new Date());
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(name+date+".csv"), "utf-8"));
				
			for(int k=0; k<result[0].length; k++){
		    	writer.write((k*timeStep)+","+result[0][k]+","+result[1][k]+"\r\n");
		    }
		} catch (IOException e) {
			warn();
		} finally {
			   try {writer.close();} catch (Exception ex) {warn();}
		} 
		
	}
	
	public static void toFile(double[][] result, double timeStep){
		Writer writer = null;
		String date = new SimpleDateFormat(" (dd-MM-yyyy HH_mm_ss)").format(new Date());
		
		for(int i=0; i<result.length; i+=2){
			try {
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("node "+(i/2+1)+" force"+date+".csv"), "utf-8"));
				
				for(int k=0; k<result[0].length; k++){
			    	writer.write((k*timeStep)+","+result[i][k]+","+result[i+1][k]+"\r\n");
			    }
			} catch (IOException e) {
				warn();
			} finally {
				   try {writer.close();} catch (Exception ex) {warn();}
			} 
		}
	}
	
	public static void toFile(double[][][] result, double timeStep){
		Writer writer = null;
		String[] names = {" displacement", " velocity"," acceleration"};
		String date = new SimpleDateFormat(" (dd-MM-yyyy HH_mm_ss)").format(new Date());
		
		for(int i=0; i<result.length; i+=2){
			for(int j=0;j<3;j++){
				try {
				    writer = new BufferedWriter(new OutputStreamWriter(
				    		new FileOutputStream("node "+(i/2+1)+names[j]+date+".csv"), "utf-8"));
				    
				    for(int k=0; k<result[0][0].length; k++){
				    	writer.write((k*timeStep)+","+result[i][j][k]+","+result[i+1][j][k]+"\r\n");
				    }
				} catch (IOException ex){
					warn();
				} finally {
				   try {writer.close();} catch (Exception ex) {warn();}
				}
			}
		}
	}
	
	public static void toFile(double[][][] result, double timeStep, double value, String name){
		Writer writer = null;
		String[] names = {" displacement", " velocity"," acceleration"};
		String date = new SimpleDateFormat(" (dd-MM-yyyy HH_mm_ss)").format(new Date());
		
		for(int i=0; i<result.length; i+=2){
			for(int j=0;j<3;j++){
				try {
				    writer = new BufferedWriter(new OutputStreamWriter(
				    		new FileOutputStream(name+" Freq"+value+names[j]+date+".csv"), "utf-8"));
				    
				    for(int k=0; k<result[0][0].length; k++){
				    	writer.write((k*timeStep)+","+result[i][j][k]+","+result[i+1][j][k]+"\r\n");
				    }
				} catch (IOException ex){
					warn();
				} finally {
				   try {writer.close();} catch (Exception ex) {warn();}
				}
			}
		}
	}
	
	public static void warn(){
		JOptionPane.showMessageDialog(null, "error writing files");
	}

}
