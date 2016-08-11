import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PhysicalProblem {
	Node[] nodes;
	Element[] elements;
	double time1, time2, timeStep;
	
	
	public PhysicalProblem(String filename, boolean truss) throws IOException, BadInputException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		
		String label = br.readLine();
		while(label!=null){
			if(truss){
				if(label.equals("Node")){
					br.readLine();
					trussNodesBuilder(br);
				}
				else if(label.equals("Element")){
					br.readLine();
					trussElementBuilder(br);
				}
			}
			else{
				if(label.equals("Interval")){
					br.readLine();
					intervalBuilder(br);
				}
				if(label.equals("Node")){
					br.readLine();
					dynamicNodeBuilder(br);
				}
				else if(label.equals("Element")){
					br.readLine();
					dynamicElementBuilder(br);
				}
			}
			label=br.readLine();
		}
	}
	
	private void dynamicElementBuilder(BufferedReader br) throws IOException{
		ArrayList in = new ArrayList();
		String read = br.readLine();
		while(!read.equals("")){
			in.add(read);
			read = br.readLine();
			if(read==null)
				break;
		}
		elements = new Element[in.size()];
		
		for(int i=0; i<elements.length; i++){
			read =(String) in.get(i);
			
			int nodeOne = (int)Double.parseDouble(read.substring(0, read.indexOf(',')));//parsed as double and cast to int to avoid number format exception from integer parsing when tabs are present
			nodeOne--;
			read = read.substring(read.indexOf(',')+1);
			
			
			int nodeTwo = (int)Double.parseDouble(read.substring(0, read.indexOf(',')));
			nodeTwo--;
			read = read.substring(read.indexOf(',')+1);
			
			
			double k = Double.parseDouble(read.substring(0, read.indexOf(',')));
			read = read.substring(read.indexOf(',')+1);
			
			double c  = Double.parseDouble(read);
			
			if(k==0&&c==0){
				throw new BadInputException(); 
			}
			if(nodeOne==nodeTwo||nodeOne<0||nodeTwo<0||nodeOne>(nodes.length-1)||nodeTwo>(nodes.length-1)){
				throw new BadInputException();
			}
			else if(nodeTwo<nodeOne){
				int temp = nodeOne;
				nodeOne = nodeTwo;
				nodeTwo = temp;
			}
			
			elements[i]=new Element(nodeOne, nodeTwo, 0, 0, k, c);
		}
	}
	
	private void dynamicNodeBuilder(BufferedReader br) throws IOException{
		ArrayList in = new ArrayList();
		String read = br.readLine();
		while(!read.equals("")){
			in.add(read);
			read = br.readLine();
			if(read==null)
				break;
		}
		nodes = new Node[in.size()];
		for(int i=0; i< nodes.length; i++){
			read =(String) in.get(i);
			
			double x =  Double.parseDouble(read.substring(0, read.indexOf(',')));
			read=read.substring(read.indexOf(',')+1);
			
			double y =  Double.parseDouble(read.substring(0, read.indexOf(',')));
			read=read.substring(read.indexOf(',')+1);
			
			double force =  Double.parseDouble(read.substring(0, read.indexOf(',')));
			read=read.substring(read.indexOf(',')+1);
			
			double angle =  Double.parseDouble(read.substring(0, read.indexOf(',')));
			read = read.substring(read.indexOf(',')+1);
			
			String type = read.substring(0, read.indexOf(',')); 
			read = read.substring(read.indexOf(',')+1);
			
			double mass =  Double.parseDouble(read);
			
			double forceX = force*Math.cos(Math.toRadians(angle));
			double forceY = force*Math.sin(Math.toRadians(angle))-mass*9.81;
			
			byte typeByte=0;
			if(type.contains("FIXED")){
				typeByte=Node.FIXED;
			}
			else if(type.contains("FREE")){
				typeByte=Node.FREE;
			}
			else if(type.contains("ROLLERX")){
				typeByte=Node.ROLLERX;
			}
			else if(type.contains("ROLLERY")){
				typeByte=Node.ROLLERY;
			}
			else{
				throw new BadInputException();
			}
			for(int j=0; j<i; j++){
				if(nodes[j].getX()==x&&nodes[j].getY()==y)
					throw new BadInputException();
			}
			nodes[i]=new Node(x, y, forceX, forceY, typeByte, mass);
		}
	}
	
	
	
	private void trussElementBuilder(BufferedReader br) throws IOException{
		ArrayList in = new ArrayList();
		String read = br.readLine();
		while(!read.equals("")){
			in.add(read);
			read = br.readLine();
			if(read==null)
				break;
		}
		
		elements = new Element[in.size()];
		
		for(int i=0; i<elements.length; i++){
			read =(String) in.get(i);
			
			int nodeOne = (int)Double.parseDouble(read.substring(0, read.indexOf(',')));//parsed as double and cast to int to avoid number format exception from integer parsing when tabs are present
			nodeOne--;
			read = read.substring(read.indexOf(',')+1);
			
			
			int nodeTwo = (int)Double.parseDouble(read.substring(0, read.indexOf(',')));
			nodeTwo--;
			read = read.substring(read.indexOf(',')+1);
			
			
			double area = Double.parseDouble(read.substring(0, read.indexOf(',')));
			read = read.substring(read.indexOf(',')+1);
			
			int modulus = (int)Double.parseDouble(read);
			
			double distX = nodes[nodeOne].getX()-nodes[nodeTwo].getX();
			double distY = nodes[nodeOne].getY()-nodes[nodeTwo].getY();
			
			
			double k = area*modulus/Math.sqrt(distX*distX+distY*distY);
			
			if(nodeOne==nodeTwo||nodeOne<0||nodeTwo<0||nodeOne>(nodes.length-1)||nodeTwo>(nodes.length-1)||area<=0||modulus<=0){
				throw new BadInputException();
			}
			else if(nodeTwo<nodeOne){
				int temp = nodeOne;
				nodeOne = nodeTwo;
				nodeTwo = temp;
			}
			
			elements[i]=new Element(nodeOne, nodeTwo, area, modulus, k, 0);
		}
	}
	
	private void trussNodesBuilder(BufferedReader br) throws IOException{
		ArrayList in = new ArrayList();
		String read = br.readLine();
		while(!read.equals("")){
			in.add(read);
			read = br.readLine();
			if(read==null)
				break;
		}
		nodes = new Node[in.size()];
		for(int i=0; i< nodes.length; i++){
			read =(String) in.get(i);
			
			double x =  Double.parseDouble(read.substring(0, read.indexOf(',')));
			read=read.substring(read.indexOf(',')+1);
			
			double y =  Double.parseDouble(read.substring(0, read.indexOf(',')));
			read=read.substring(read.indexOf(',')+1);
			
			double force =  Double.parseDouble(read.substring(0, read.indexOf(',')));
			read=read.substring(read.indexOf(',')+1);
			
			double angle =  Double.parseDouble(read.substring(0, read.indexOf(',')));
			
			String type = read.substring(read.indexOf(',')+1); 
			
			double forceX = force*Math.cos(Math.toRadians(angle));
			double forceY = force*Math.sin(Math.toRadians(angle));
			
			byte typeByte=0;
			if(type.contains("FIXED")){
				typeByte=Node.FIXED;
			}
			else if(type.contains("FREE")){
				typeByte=Node.FREE;
			}
			else if(type.contains("ROLLERX")){
				typeByte=Node.ROLLERX;
			}
			else if(type.contains("ROLLERY")){
				typeByte=Node.ROLLERY;
			}
			else{
				throw new BadInputException();
			}
			for(int j=0; j<i; j++){
				if(nodes[j].getX()==x&&nodes[j].getY()==y)
					throw new BadInputException();
			}
			nodes[i]=new Node(x, y, forceX, forceY, typeByte, 0);
		}
	}
	
	private void intervalBuilder(BufferedReader br) throws IOException{
		String in = br.readLine();
		time1 = Double.parseDouble(in.substring(0, in.indexOf(',')));
		in=in.substring(in.indexOf(',')+1);
		time2 = Double.parseDouble(in.substring(0, in.indexOf(','))); 
		in=in.substring(in.indexOf(',')+1);
		timeStep = Double.parseDouble(in); 
	}
	
	public Node[] getNodes(){
		return nodes;
	}
	
	public Element[] getElements(){
		return elements;
	}
	public double getTime1(){ 
        return time1; 
    } 
      
    public double getTime2(){ 
        return time2; 
    } 
      
    public double getTimeStep(){ 
        return timeStep; 
    } 
}
