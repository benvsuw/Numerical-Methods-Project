
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ElectricProblem {
	E_Element[] elements;

	public ElectricProblem(String filename) throws IOException, BadInputException{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			br.readLine();
			
			ArrayList in = new ArrayList();
			String read = br.readLine();
			while(!read.equals("")){
				in.add(read);
				read = br.readLine();
				if(read==null)
					break;
			}
			
			elements = new E_Element[in.size()];
			
			for(int i=0; i<elements.length; i++){
				read = (String) in.get(i);
				
				int loopOne = (int) Double.parseDouble(read.substring(0, read.indexOf(',')));
				read=read.substring(read.indexOf(',')+1);
				
				String temp = read.substring(0, read.indexOf(','));
				int loopTwo=-1;
				boolean loopTwoPresent=false;
				if (temp.length()>0){
					loopTwo = (int) Double.parseDouble(temp);
					loopTwoPresent = true;
				}
				read=read.substring(read.indexOf(',')+1);
				
				temp = read.substring(0, read.indexOf(','));
				read=read.substring(read.indexOf(',')+1);
				
				double val;
				
				byte type=-1;
				
				if(read.contains("voltage source")){
					type = E_Element.VSOURCE;
					if(loopTwoPresent){
						elements[i]=new E_Element(loopOne, loopTwo, temp, type);
					}
					else{
						elements[i]=new E_Element(loopOne, temp, type);
					}
				}
				else{
					val = Double.parseDouble(temp);
					if(read.contains("resistor")){
						type = E_Element.RES;
					}
					else if(read.contains("inductor")){
						type = E_Element.IND;
					}
					else if(read.contains("capacitor")){
						type = E_Element.CAP;
					}
					
					if(loopTwoPresent){
						elements[i]=new E_Element(loopOne, loopTwo, val, type);
					}
					else{
						elements[i]=new E_Element(loopOne, val, type);
					}
				}
				
			}
	}
	public E_Element[] getElements(){
		return elements;
	}
}
