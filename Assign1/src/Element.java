import static java.lang.Math.*;

public class Element {
	int nodeOne;
	int nodeTwo;
	double area;
	double modulus;
	double kVal;
	double cVal;
	
	public Element (int nodeOneIn, int nodeTwoIn, double areaIn, int modulusIn, double kValIn, double cValIn){
		
		nodeOne = nodeOneIn;
		nodeTwo = nodeTwoIn;
		area = areaIn;
		modulus = modulusIn;
		kVal = kValIn;
		cVal = cValIn;
		
	}
	
	public int getNodeOne(){
		return nodeOne;
	}
	
	public int getNodeTwo(){
		return nodeTwo;
	}
	
	public double getArea(){
		return area;
	}
	
	public double getModulus(){
		return modulus;
	}
	
	public double[][] getLocalK (Node [] node) {
		
		double[][] LocalK = new double[4][4];
		double x1 = node[nodeOne].getX();
		double y1 = node[nodeOne].getY();
		double x2 = node[nodeTwo].getX();
		double y2 = node[nodeTwo].getY();
		
		double xDiff = x2 - x1;
		double yDiff = y2 - y1;
		double hyp = sqrt(xDiff*xDiff + yDiff*yDiff);
		
		double a = xDiff/hyp;
		double b = yDiff/hyp;
		
		LocalK[0][0] =  (kVal*a*a);
		LocalK[0][1] =  (kVal*a*b);
		LocalK[0][2] = -(kVal*a*a);
		LocalK[0][3] = -(kVal*a*b);
				
		LocalK[1][0] =  (kVal*a*b);
		LocalK[1][1] =  (kVal*b*b);
		LocalK[1][2] = -(kVal*a*b);	
		LocalK[1][3] = -(kVal*b*b);
		
		LocalK[2][0] = -(kVal*a*a);
		LocalK[2][1] = -(kVal*a*b); 
		LocalK[2][2] = 	(kVal*a*a);
		LocalK[2][3] =  (kVal*a*b);
	
		LocalK[3][0] = -(kVal*a*b);
		LocalK[3][1] = -(kVal*b*b);
		LocalK[3][2] =  (kVal*a*b);
		LocalK[3][3] =  (kVal*b*b);
		
		return LocalK;
	}
	
	public double[][] getLocalC (Node [] node) {
		
		double[][] LocalC = new double[4][4];
		double x1 = node[nodeOne].getX();
		double y1 = node[nodeOne].getY();
		double x2 = node[nodeTwo].getX();
		double y2 = node[nodeTwo].getY();
		
		double xDiff = x2 - x1;
		double yDiff = y2 - y1;
		double hyp = sqrt(xDiff*xDiff + yDiff*yDiff);
		
		double a = xDiff/hyp;
		double b = yDiff/hyp;
		
		LocalC[0][0] =  (cVal*a*a);
		LocalC[0][1] =  (cVal*a*b);
		LocalC[0][2] = -(cVal*a*a);
		LocalC[0][3] = -(cVal*a*b);
				
		LocalC[1][0] =  (cVal*a*b);
		LocalC[1][1] =  (cVal*b*b);
		LocalC[1][2] = -(cVal*a*b);	
		LocalC[1][3] = -(cVal*b*b);
		
		LocalC[2][0] = -(cVal*a*a);
		LocalC[2][1] = -(cVal*a*b); 
		LocalC[2][2] = 	(cVal*a*a);
		LocalC[2][3] =  (cVal*a*b);
	
		LocalC[3][0] = -(cVal*a*b);
		LocalC[3][1] = -(cVal*b*b);
		LocalC[3][2] =  (cVal*a*b);
		LocalC[3][3] =  (cVal*b*b);
		
		return LocalC;
	}
}
