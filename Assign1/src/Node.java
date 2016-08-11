
public class Node {

	public static final byte FREE = 0, FIXED = 1, ROLLERX = 2, ROLLERY = 3; 
	private double x, y, xForce, yForce, mass;
	private byte type; 
	
	//Constructors
	public Node(){
	
		x=0;
		y=0;
		xForce=0;
		yForce=0;
		type=FREE;
		mass=0;
		
	}
	
	public Node(double xi, double yi) {
		
		x=xi;
		y=yi;
		xForce=0;
		yForce=0;
		type=FREE;
		mass=0;
		
	}
	
	public Node(double xi, double yi, double xForcei, double yForcei, byte typei, double massi) {
		
		x=xi;
		y=yi;
		xForce=xForcei;
		yForce=yForcei;
		type=typei;
		mass=massi;
		
	}
	
	// Public Get functions
	public double getX(){
		
		return x;
	}
	
	public double getY(){
		
		return y;
	}
	
	public byte getType() {
		
		return type;
	}
	
	public double getXForce() {
		
		return xForce;
	}
	
	public double getYForce() {
		
		return yForce;
	}
	
	public double getMass() {
		
		return mass;
	}
	
}
