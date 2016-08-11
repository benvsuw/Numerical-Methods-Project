public class E_Element {

	public static final byte RES = 0, IND = 1, CAP = 2, VSOURCE=3, ISOURCE=4; 
	
	int loopOne;
	int loopTwo;
	double value;
	String vsource;
	byte type;
	
	public E_Element () {
		
		loopOne = -1;
		loopTwo = -1;
		value = 0;
		type = -1;
		
		vsource=null;		
	}
	
	public E_Element (int loopOneIn, double valueIn, byte typeIn) {
		
		loopOne = loopOneIn;
		loopTwo = -1;
		value=valueIn;
		type=typeIn;

		vsource = null;
	}
	
	public E_Element (int loopOneIn, int loopTwoIn, double valueIn, byte typeIn) {
		
		loopOne = loopOneIn;
		loopTwo = loopTwoIn;
		value = valueIn;
		type = typeIn;
		
		vsource = null;
		
	}
	
	//Voltage source
	public E_Element (int loopOneIn, int loopTwoIn, String valueIn, byte typeIn) {
		
		loopOne = loopOneIn;
		loopTwo = loopTwoIn;
		vsource = valueIn;
		type = typeIn;
		
		value = 0;
		
	}
	
	public E_Element (int loopOneIn, String valueIn, byte typeIn) {
		
		loopOne = loopOneIn;
		loopTwo = -1;
		vsource = valueIn;
		type = typeIn;
		
		value = 0;
		
	}
	
	//public get functions
	public int getLoopOne() {
		return loopOne;
	}
	
	public int getLoopTwo () {
		return loopTwo;
	}
	
	public double getValue() {
		return value;
	}
	
	public String getString() {
		return vsource;
	}
	
	public byte getType () {
		return type;
	}
	
	public double getResistance () {
		if (type == RES) {
			return value;
		}
		else return 0;
	}
	
	public double getInductance () {
		if (type == IND) {
			return value;
		}
		else return 0;
	}
	
	public double getCapacitance () {
		if (type == CAP) {
			return value;
		}
		else return 0;
	}
	
	public double getVSource () {
		if (type == VSOURCE) {
			return value;
		}
		else return 0;
	}
	
	public double getISource () {
		if (type == ISOURCE) {
			return value;
		}
		else return 0;
	}
	
	// Local Matrix
	public double [][] getLocalMatrix() {
		
		double [][] k = new double[2][2];
		
		if (loopOne != -1 && loopTwo != -1) {
			k[0][0] = value;
			k[0][1] = -value;
			k[1][0] = -value;
			k[1][1] = value;
		}
		else if (loopOne != -1) {
			k[0][0] = value;
			k[0][1] = 0;
			k[1][0] = 0;
			k[1][1] = 0;
		}
		
		return k;
	}
	
	// Private Set Functions
	/*
	private void setLoopOne (int loop) {
		loopOne = loop;
		return;
	}
	
	private void setLoopTwo (int loop) {
		loopTwo = loop;
		return;
	}
	
	private void setLoops (int loop1, int loop2) {
		loopOne = loop1;
		loopTwo = loop2;
		return;
	}
	
	private void setValue (int val) {
		value = val;
		return;
	}
	
	private void setResistance (double val) {
		value = val;
		type = RES;
		return;
	}
	
	private void setInductance (double val) {
		value = val;
		type = IND;
		return;
	}
	
	private void setCapacitance (double val) {
		value = val;
		type = CAP;
		return;
	}
	
	private void setVSource (double val) {
		value = val;
		type = VSOURCE;
		return;
	}
	
	private void setISource (double val) {
		value = val;
		type = ISOURCE;
		return;
	}
	
	private void setType (byte typeIn) {
		type = typeIn;
		return;
	}
	*/
	
}
