import static java.lang.Math.*;

import java.util.Arrays;

import javax.swing.JOptionPane;

public class Solver {
	
	public static final byte Q1 = 0, Q2 = 1, Q3 = 2, Q4I = 3, Q4II = 4; 
	private static final int graphInterval = 1000;
	
	public Solver(){
		}
	
	private static double [] getReactionForces (Node [] nodes, Element [] elements, double [][] elementsStress, double [] displacements){
		int numOfNodes = nodes.length;
		int numOfElements = elements.length;
		double [] reactionForces = new double [numOfNodes*2];
		for (int i = 0; i < numOfNodes; i++){
			byte type = nodes[i].getType();
			if (type == Node.FIXED){
				for(int j = 0; j < numOfElements; j++){
					int nodeOne = elements[j].getNodeOne();
					int nodeTwo = elements[j].getNodeTwo();
					if (nodeOne == i || nodeTwo == i){
						double tempForce = elementsStress[j][2]*elements[j].getArea()*(-1);
						double x = abs (nodes[nodeTwo].getX() - nodes[nodeOne].getX() + displacements [nodeTwo*2] - displacements[nodeOne*2]);
						double y = abs (nodes[nodeTwo].getY() - nodes[nodeOne].getY() + displacements [nodeTwo*2+1] - displacements[nodeOne*2+1]);
						double h = sqrt (x*x + y*y);
						reactionForces [i] += tempForce*x/h;
						reactionForces [i+1] += tempForce*y/h;
					}
				}
			}else if (type == Node.ROLLERX){
				for(int j = 0; j < numOfElements; j++){
					int nodeOne = elements[j].getNodeOne();
					int nodeTwo = elements[j].getNodeTwo();
					if (nodeOne == i || nodeTwo == i){
						double tempForce = elementsStress[j][2]*elements[j].getArea()*(-1);
						double x = abs (nodes[nodeTwo].getX() - nodes[nodeOne].getX() + displacements [nodeTwo*2] - displacements[nodeOne*2]);
						double y = abs (nodes[nodeTwo].getY() - nodes[nodeOne].getY() + displacements [nodeTwo*2+1] - displacements[nodeOne*2+1]);
						double h = sqrt (x*x + y*y);
						reactionForces [i] += 0;
						reactionForces [i+1] += tempForce*y/h;
					}
				}
			}else if (type == Node.ROLLERY){
				for(int j = 0; j < numOfElements; j++){
					int nodeOne = elements[j].getNodeOne();
					int nodeTwo = elements[j].getNodeTwo();
					if (nodeOne == i || nodeTwo == i){
						double tempForce = elementsStress[j][2]*elements[j].getArea()*(-1);
						double x = abs (nodes[nodeTwo].getX() - nodes[nodeOne].getX() + displacements [nodeTwo*2] - displacements[nodeOne*2]);
						double y = abs (nodes[nodeTwo].getY() - nodes[nodeOne].getY() + displacements [nodeTwo*2+1] - displacements[nodeOne*2+1]);
						double h = sqrt (x*x + y*y);
						reactionForces [i] += tempForce*x/h;
						reactionForces [i+1] += 0;
					}
				}
			}			
		}
		return reactionForces;
	}
	
	public static void TrussStress(Element [] elements, Node [] nodes){		
		
		if (nodes == null ||elements == null){
			warnIn ();
			throw new IllegalArgumentException("Invalid input parameters");
		}
		
		double [][] globalK = getGlobalK(nodes, elements);
		double [] forces = getForceMatrixMath(nodes);
		globalK = applyBC(nodes, globalK);
		boolean [] isZero = new boolean [globalK.length]; // determined if row/column of index is zero
		Arrays.fill(isZero, true);
		int numRemovedAti [] = new int [globalK.length]; // used to remember correct position in MatrixMath
		int numRemoved = 0;
		int size  = globalK.length;
		double [][] revisedGlobalK=globalK;
		
		for (int i = 0; i<size; i++){
			for (int j = 0; j<size; j++){
				 isZero[i] = ((globalK[i][j]==0)&&isZero[i]==true);
			}
			numRemovedAti[i]=numRemoved;
			if (isZero[i] == true){
				if (numRemoved + 1 == size){
					warnConstrained();
					throw new IllegalArgumentException("Illegal input parameters");
				}
				revisedGlobalK = removeZero(revisedGlobalK, i-numRemoved);
				forces = removeZero(forces, i-numRemoved);
				numRemoved++;
			}
		}
		double [] intermediateDisplacments = MatrixMath.Multiply (MatrixMath.Invert(revisedGlobalK), forces);
			
		int numOfElements = elements.length;
		double [][] elementStress = new double [numOfElements][3]; //stores nodeOne, nodeTwo, and abs(Stress) to print
		String [] state = new String [numOfElements]; // stores compression or tension
		double tempStress;
		
		int numOfNodes = nodes.length;
		double [] displacements = new double [numOfNodes*2];
		
		for (int i = 0; i < numOfNodes*2; i++){
			if (isZero[i] == true)
				displacements [i] = 0;
			else 
				displacements [i] = intermediateDisplacments [i-numRemovedAti[i]];
		}
		
		for (int i = 0; i < numOfElements; i++){
			int nodeOne = elements[i].getNodeOne();
			int nodeTwo = elements[i].getNodeTwo();
			
			double nodeOneX = nodes[nodeOne].getX();
			double nodeOneY = nodes[nodeOne].getY();
			double nodeTwoX = nodes[nodeTwo].getX();
			double nodeTwoY = nodes[nodeTwo].getY();
			
			double nodeOneXF, nodeOneYF, nodeTwoXF, nodeTwoYF;
			nodeOneXF = nodeOneX + displacements  [nodeOne*2];
			nodeOneYF = nodeOneY + displacements  [nodeOne*2+1];		
			nodeTwoXF = nodeTwoX + displacements  [nodeTwo*2];
			nodeTwoYF = nodeTwoY + displacements  [nodeTwo*2+1];
									
			double origLength = sqrt((nodeTwoX-nodeOneX)*(nodeTwoX-nodeOneX) + (nodeTwoY-nodeOneY)*(nodeTwoY-nodeOneY));
			double finalLength = sqrt((nodeTwoXF-nodeOneXF)*(nodeTwoXF-nodeOneXF) + (nodeTwoYF-nodeOneYF)*(nodeTwoYF-nodeOneYF));
			
			tempStress = elements[i].getModulus()*(finalLength-origLength)/origLength;
			
			if (tempStress >= 0)
				state[i] = "TENSION";
			else
				state[i] = "COMPRESSION";
			
			elementStress[i][0] = nodeOne + 1;
			elementStress[i][1] = nodeTwo + 1;
			elementStress[i][2] = tempStress;			
		}
		
		double [] reactionForces =  Solver.getReactionForces(nodes, elements, elementStress, displacements);
 		for (int i = 0; i < numOfElements; i++){
			elementStress[i][2] = abs (elementStress[i][2]);
		}
		
		
		filer.trussToFile (elementStress, state, displacements, reactionForces);		
	}
	
	public static void solveElectrical(E_Element [] elements) {
		
		int numOfLoops = 0;
		for (int i =0; i < elements.length; i++) {
			if (elements[i].getLoopOne() > numOfLoops) numOfLoops = elements[i].getLoopOne(); 
			else if (elements[i].getLoopTwo() > numOfLoops) numOfLoops = elements[i].getLoopTwo();
		}
		
		int numOfElements = elements.length;
		
		double [][] globalL = new double[numOfLoops][numOfLoops];
		double [][] globalR = new double[numOfLoops][numOfLoops];
		double [][] globalC = new double[numOfLoops][numOfLoops];
		String [] globalV = new String[numOfLoops];
		
		for (int i=0; i<numOfElements; i++) {
			if (elements[i].getType() == E_Element.IND) {
				
				int loop1 = elements[i].getLoopOne()-1;
				int loop2 = elements[i].getLoopTwo()-1;
				
				double [][] local = elements[i].getLocalMatrix();
				
				if (loop2 < 0) {
					globalL[loop1][loop1] += local[0][0];
				}
				else {
					globalL[loop1][loop1] += local[0][0];
					globalL[loop1][loop2] += local[0][1];
					globalL[loop2][loop1] += local[1][0];
					globalL[loop2][loop2] += local[1][1];
				}
			}
			else if (elements[i].getType() == E_Element.RES) {
				
				int loop1 = elements[i].getLoopOne() -1;
				int loop2 = elements[i].getLoopTwo() -1;
				
				double [][] local = elements[i].getLocalMatrix();
				
				if (loop2 < 0) {
					globalR[loop1][loop1] += local[0][0];
				}
				else {
					globalR[loop1][loop1] += local[0][0];
					globalR[loop1][loop2] += local[0][1];
					globalR[loop2][loop1] += local[1][0];
					globalR[loop2][loop2] += local[1][1];
				}
			}
			else if (elements[i].getType() == E_Element.CAP) {
				int loop1 = elements[i].getLoopOne() -1;
				int loop2 = elements[i].getLoopTwo() -1;
				
				double [][] local = elements[i].getLocalMatrix();
				
				if (loop2 < 0) {
					globalC[loop1][loop1] += local[0][0];
				}
				else {
					globalC[loop1][loop1] += local[0][0];
					globalC[loop1][loop2] += local[0][1];
					globalC[loop2][loop1] += local[1][0];
					globalC[loop2][loop2] += local[1][1];
				}
			}
			else if (elements[i].getType() == E_Element.VSOURCE) {
					
				globalV[elements[i].getLoopOne()-1] = elements[i].getString();
			}
		}
		
		filer.printElectric(globalR, globalL, globalC, globalV);
	}

	public static void solveMassSpringDamper (Node [] nodes, 
											  Element [] elements, 
											  double t1, 
											  double t2, 
											  double tDelta, 
											  byte questionType){
		
		if (nodes == null ||elements == null || t1 > t2 || tDelta <=0 ){
			warnIn();
			throw new IllegalArgumentException("Invalid input parameters");
		}
		
		double [][] globalK = getGlobalK(nodes, elements);
		double [][] globalC = getGlobalC(nodes, elements);
		double [][] globalM = getGlobalM(nodes);
		double [] forces = new double [globalK.length];
		
		if (questionType == Q4I)
			Arrays.fill(forces, 0); //ignore gravity for first part, sinusoidal force applied later on during linear time integration
		else 
			forces = getForceMatrixMath(nodes);//gravity exists get MatrixMath, NOTE: applied force of 20t for Q4II applied during linear time integration
		
		// Apply BC
		globalK =  applyBC(nodes, globalK);
		
		boolean [] isZero = new boolean [nodes.length*2]; // determined if row/column of index is zero
		Arrays.fill(isZero, true);
		int numRemovedAti [] = new int [nodes.length*2]; // used to remember correct position in MatrixMath
		int numRemoved = 0;
		int size  = globalK.length;
		double [][] revisedGlobalK=globalK;
		
		for (int i = 0; i<size; i++){
			for (int j = 0; j<size; j++){
				 isZero[i] = ((globalK[i][j]==0)&&isZero[i]==true);
			}
			numRemovedAti[i]=numRemoved;
			if (isZero[i] == true){
				if (numRemoved + 1 == size){
					warnConstrained();
					throw new IllegalArgumentException("Illegal input parameters");
				}
				revisedGlobalK = removeZero(revisedGlobalK, i-numRemoved);
				forces = removeZero(forces, i-numRemoved);
				globalM = removeZero(globalM, i-numRemoved);
				globalC = removeZero(globalC, i-numRemoved);
				numRemoved++;
			}
		}
		
		globalK = revisedGlobalK;
		
		int iterations = (int)((t2 - t1)/tDelta); //design decision to truncate down 
		size = globalK.length;
		double [][][] results;
		double [][] A_inverse = calculateAInverse(globalM, globalC, tDelta);
		double [] uCurr = new double [size];
		double [] uNext = new double [size];
		double [] uPrev = new double [size];
		int print_size = 0;
		int print_interval = 0;
		int print_count = 0;
		
		if (iterations > graphInterval)
			print_size = graphInterval;
		else
			print_size = iterations;
		
		print_interval = iterations/print_size;
		
		if (questionType == Q3){
			results = new double [nodes.length*2][3][print_size];
			
			for (int i = 0; i < iterations; i++){
				uNext = numericalTimeIntegration(uCurr, uPrev, forces, globalK, globalC, globalM, A_inverse, tDelta);
				
				if (i % print_interval == 0 && print_count < print_size){
					for (int j = 0; j < nodes.length*2; j++){
						if (isZero[j] == false){
							results [j][0][print_count] = uCurr[j-numRemovedAti[j]];
							results [j][1][print_count] = velocity (uNext[j-numRemovedAti[j]], uPrev[j-numRemovedAti[j]], tDelta); 
							results [j][2][print_count] = acceleration (uNext[j-numRemovedAti[j]], uCurr[j-numRemovedAti[j]], uPrev[j-numRemovedAti[j]], tDelta);
						}
					}
					print_count++;
				}	
				uPrev = uCurr;
				uCurr = uNext;
			}
			filer.toFile(results, (t2-t1)/print_size); //print displacement, velocity, acceleration for all nodes to graph
			
		}else if (questionType == Q4I){
			//to correlate to correct MatrixMath position due to removal of rows/columns of zeros 
			int node7XMarker = 12 - numRemovedAti[12];
			int node3XMarker = 4 - numRemovedAti[4];
			int node3YMarker = 5 - numRemovedAti[5];
			results = new double [2][3][print_size]; //only graphing information for node 3
			
			double peak3X = 0;
			double peak3Y = 0;

			double meanSteadyStateResponse3X = 0;
			double meanSteadyStateResponse3Y = 0;
			
			double [][] printPeak = new double [5][3];
			double [][] printAmp = new double [5][3];
			double [][] printSteadyStateResponse = new double [5][3];
			double [][] freqSweep = new double [299][3];
			int wPrinted = 0;
			
			for (double w = 1; w <=150; w+=0.5 ){
				boolean wPrint = false;
				double tOneCycle = 25+2*PI/w;
				if (w == 1 || w == 3.5 || w == 7 || w == 10 || w == 30)
					wPrint = true;
				
				for (int i = 0; i < iterations; i++){
					//adjust applied force as it varies by time
					forces [node7XMarker] = 10*(sin(w*tDelta*i));
					
					uNext = numericalTimeIntegration(uCurr, uPrev, forces, globalK, globalC, globalM, A_inverse, tDelta);
					
					if (wPrint && i % print_interval == 0 && print_count < print_size){
						//Store node 3 X values
						results [0][0][print_count] = uCurr[node3XMarker];
						results [0][1][print_count] = velocity (uNext[node3XMarker], uPrev[node3XMarker], tDelta); 
						results [0][2][print_count] = acceleration (uNext[node3XMarker], uCurr[node3XMarker], uPrev[node3XMarker], tDelta);
						
						//Store node 3 Y values
						results [1][0][print_count] = uCurr[node3YMarker];
						results [1][1][print_count] = velocity (uNext[node3YMarker], uPrev[node3YMarker], tDelta); 
						results [1][2][print_count] = acceleration (uNext[node3YMarker], uCurr[node3YMarker], uPrev[node3YMarker], tDelta);
						print_count ++;
					}
					
					//Find peak for node 3, during steady state response
					if (i*tDelta >= 25){
							peak3X = max(uCurr[node3XMarker], peak3X);
							peak3Y = max(uCurr[node3YMarker], peak3Y);
					}
					
					// find mean stead state response for one cycle 
					if (i*tDelta >= 25 && i*tDelta<=(tOneCycle)){
						meanSteadyStateResponse3X += calculateSteadyStateResponse(uCurr[node3XMarker], uPrev[node3XMarker], tDelta);
						meanSteadyStateResponse3Y += calculateSteadyStateResponse(uCurr[node3YMarker], uPrev[node3YMarker], tDelta);
					}
					
					//reset for next iteration
					uPrev = uCurr;	
					uCurr = uNext;
				}

				//divide by time interval to complete formula
				meanSteadyStateResponse3X = meanSteadyStateResponse3X/(2*PI/w);
				meanSteadyStateResponse3Y = meanSteadyStateResponse3Y/(2*PI/w);
				
				double amp3UX = abs (meanSteadyStateResponse3X - peak3X);
				double amp3UY = abs (meanSteadyStateResponse3Y - peak3Y);
				
				if (wPrint == true){
					printPeak [wPrinted][0] = w;
					printPeak [wPrinted][1] = peak3X;
					printPeak [wPrinted][2] = peak3Y;

					printSteadyStateResponse [wPrinted][0] = w;
					printSteadyStateResponse [wPrinted][1] = meanSteadyStateResponse3X;
					printSteadyStateResponse [wPrinted][2] = meanSteadyStateResponse3Y;

					printAmp [wPrinted][0] = w;
					printAmp [wPrinted][1] = amp3UX;
					printAmp [wPrinted][2] = amp3UY;
					
					wPrinted++;
					filer.toFile(results, (t2-t1)/print_size, w, "Node3"); //print displacement, velocity, acceleration for node 3 at req freq to graph
				}
				
				freqSweep[(int)(w*2-2)][0] = w;
				freqSweep[(int)(w*2-2)][1] = amp3UX;
				freqSweep[(int)(w*2-2)][2] = amp3UY;
				
				//Reset all values back to 0 for next iteration at new freq
				print_count = 0;
				meanSteadyStateResponse3X = 0;
				meanSteadyStateResponse3Y = 0; 
				peak3X = 0;
				peak3Y = 0;
				Arrays.fill(uCurr, 0);
				Arrays.fill(uPrev, 0);
				Arrays.fill(uNext, 0);	
			}
			
			//Print remaining desired information to tables for graphing and evaluation 
			filer.printTable (printPeak, "Node_3_Peak");
			filer.printTable (printSteadyStateResponse, "Node_3_Steady_State_Response");
			filer.printTable (printAmp, "Node_3_Amp");
			filer.printTable (freqSweep, "Frequency_Sweep_on_Node_3");
			
		}else if (questionType == Q4II){
			int node7XMarker = 12 - numRemovedAti[12];
			results = new double [nodes.length*2][3][print_size];
			for (int i = 0; i < iterations; i++){
				forces [node7XMarker] = 20*i*tDelta;
				uNext = numericalTimeIntegration(uCurr, uPrev, forces, globalK, globalC, globalM, A_inverse, tDelta);
				if (i % print_interval == 0 && print_count < print_size){
					for (int j = 0; j < nodes.length*2; j++){
						if (isZero[j] == false){
							results [j][0][print_count] = uCurr[j-numRemovedAti[j]];
							results [j][1][print_count] = velocity (uNext[j-numRemovedAti[j]], uPrev[j-numRemovedAti[j]], tDelta); 
							results [j][2][print_count] = acceleration (uNext[j-numRemovedAti[j]], uCurr[j-numRemovedAti[j]], uPrev[j-numRemovedAti[j]], tDelta);
						}
					}
					print_count++;
				}
				uPrev = uCurr;	
				uCurr = uNext;
			}
			filer.toFile(results, (t2-t1)/print_size); //print displacement, velocity, acceleration for all nodes to graph
		}
	}
	
	private static double[][] getGlobalK (Node [] nodes, Element [] elements) {

		int numOfNodes = nodes.length;
		int numOfElements = elements.length;
		
		int kSize = numOfNodes * 2; //2 DOF
		
		double[][] Globalk = new double[kSize][kSize];

		for (int i=0; i<numOfElements; i++){
			int node1 = elements[i].getNodeOne() * 2;
			int node2 = elements[i].getNodeTwo() * 2;
			double [][] tempLocalK = elements[i].getLocalK(nodes);
			
			Globalk[node1][node1] += tempLocalK[0][0];
			Globalk[node1][node1+1] += tempLocalK[0][1];
			Globalk[node1+1][node1] += tempLocalK[1][0];
			Globalk[node1+1][node1+1] += tempLocalK[1][1];
			
			Globalk[node1][node2] += tempLocalK[0][2];
			Globalk[node1][node2+1] += tempLocalK[0][3];
			Globalk[node1+1][node2] += tempLocalK[1][2];
			Globalk[node1+1][node2+1] += tempLocalK[1][3];
			
			Globalk[node2][node1] += tempLocalK[2][0];
			Globalk[node2][node1+1] += tempLocalK[2][1];
			Globalk[node2+1][node1] += tempLocalK[3][0];
			Globalk[node2+1][node1+1] += tempLocalK[3][1];
			
			Globalk[node2][node2] += tempLocalK[2][2];
			Globalk[node2][node2+1] += tempLocalK[2][3];
			Globalk[node2+1][node2] += tempLocalK[3][2];
			Globalk[node2+1][node2+1] += tempLocalK[3][3];
			
		}
		return Globalk;
	} 
	
	private static double[][] getGlobalC (Node [] nodes, Element [] elements) {

		int numOfNodes = nodes.length;
		int numOfElements = elements.length;
		
		int kSize = numOfNodes * 2; //2 DOF
		
		double[][] GlobalC = new double[kSize][kSize];

		for (int i=0; i<numOfElements; i++){
			int node1 = elements[i].getNodeOne() * 2;
			int node2 = elements[i].getNodeTwo() * 2;
			double [][] tempLocalC = elements[i].getLocalC(nodes);
			
			GlobalC[node1][node1] += tempLocalC[0][0];
			GlobalC[node1][node1+1] += tempLocalC[0][1];
			GlobalC[node1+1][node1] += tempLocalC[1][0];
			GlobalC[node1+1][node1+1] += tempLocalC[1][1];
			
			GlobalC[node1][node2] += tempLocalC[0][2];
			GlobalC[node1][node2+1] += tempLocalC[0][3];
			GlobalC[node1+1][node2] += tempLocalC[1][2];
			GlobalC[node1+1][node2+1] += tempLocalC[1][3];
			
			GlobalC[node2][node1] += tempLocalC[2][0];
			GlobalC[node2][node1+1] += tempLocalC[2][1];
			GlobalC[node2+1][node1] += tempLocalC[3][0];
			GlobalC[node2+1][node1+1] += tempLocalC[3][1];
			
			GlobalC[node2][node2] += tempLocalC[2][2];
			GlobalC[node2][node2+1] += tempLocalC[2][3];
			GlobalC[node2+1][node2] += tempLocalC[3][2];
			GlobalC[node2+1][node2+1] += tempLocalC[3][3];
		}	
		return GlobalC;
	} 
	
	private static double [][] getGlobalM (Node [] nodes){
		int mSize = nodes.length*2;
		double tempMass = 0;
		
		double [][] GlobalM = new double [mSize][mSize];
		
		for (int i = 0, j =0; i < mSize; i+=2, j++){
			tempMass = nodes[j].getMass();
			GlobalM[i][i] = tempMass;
			GlobalM[i+1][i+1] = tempMass;
		}
		return GlobalM;
	}
	
	private static double [][] calculateAInverse (double [][] globalM, double [][] globalC, double time){
	
		int size = globalC.length;
		double [][] A = new double [size][size];

		//Calculate the A MatrixMathMath (nxn) 
		A = MatrixMath.Add(MatrixMath.Multiply(globalM,(1/(time*time))), 
						MatrixMath.Multiply(globalC, 1/(2*time)));
		
		return MatrixMath.Invert(A);
	}

	private static double [] numericalTimeIntegration ( 
											double [] uCurr,
											double [] uPrev,
											double [] force,
											double [][] globalK, 
											double [][] globalC,  
											double [][] globalM,
											double [][]A_inverse,
											double time			  ) {
		int size = globalK.length;

		double [] G = new double [size];
		double [] uNext = new double [size];
		
		//Calculating G MatrixMath (nx1)
		double [] part1 = MatrixMath.Multiply(MatrixMath.Subtract(MatrixMath.Multiply(globalM, (2/(time*time))),
										globalK), 
						uCurr);
		
		double [] part2 = MatrixMath.Multiply(MatrixMath.Subtract(MatrixMath.Multiply(globalM, 1/(time*time)), 
														  MatrixMath.Multiply(globalC, 1/(2*time))),
										  uPrev);
		
		G = MatrixMath.Add(MatrixMath.Subtract(part1, part2),
				force);
		
		//Calc uNext
		uNext = MatrixMath.Multiply(A_inverse, G);
		return uNext; 
	}
	
	private static double[][] applyBC(Node [] nodes, double [][] GlobalK){
		int numOfNodes = nodes.length;
		for (int i=0; i<numOfNodes; i++){
			if (nodes[i].getType() == Node.ROLLERY){
				for (int j=0; j<numOfNodes*2; j++){
					GlobalK[i*2][j] = 0;
					GlobalK[j][i*2] = 0;
				}
			}else if(nodes[i].getType() == Node.ROLLERX){
				for (int j=0; j<numOfNodes*2; j++){
					GlobalK[i*2+1][j] = 0;
					GlobalK[j][i*2+1] = 0;
				}
			}else if(nodes[i].getType() == Node.FIXED){
				for (int j=0; j<numOfNodes*2; j++){
					GlobalK[i*2][j] = 0;
					GlobalK[j][i*2] = 0;
					GlobalK[i*2+1][j] = 0;
					GlobalK[j][i*2+1] = 0;
				}				
			}
		}
		return GlobalK;
	}
		
	private static double [] getForceMatrixMath (Node [] nodes){
		int size = nodes.length * 2; 
		double [] forces = new double [size];
		for (int i = 0; i < nodes.length; i++){
			forces[i*2] = nodes[i].getXForce();
			forces[i*2+1] = nodes [i].getYForce(); 
		}
		return forces;
	}

	private static double [][] removeZero(double [][] array, int remove){
		int size = array.length;
		double [][] newArray = new double [size-1][size-1];
		for (int i = 0, k = 0; i < size -1; i++, k++){
			if (k == remove)
				k++;
			for (int j = 0, l = 0; j < size -1; j++, l++){
				if (l == remove)
					l++;
				newArray[i][j]=array[k][l];
			}
		}
		return newArray;
	}
	
	private static double [] removeZero(double [] array, int remove){
		int size = array.length;
		double [] newArray = new double [size-1];
		for (int i = 0, k = 0; i < size -1; i++, k++){
			if (k == remove)
				k++;
			newArray[i]=array[k];
		}
		return newArray;
	}
		
	private static double calculateSteadyStateResponse(double curr, double prev, double time){
		return (curr + prev)*time/2;
	}
	
	private static double velocity (double uNext, double uPrev, double time){
		return (uNext - uPrev)/(2*time);
	}

	private static double acceleration (double uNext, double uCurr, double uPrev, double time){
		return (uNext - 2*uCurr + uPrev)/(time*time);
	}

	private static void warnConstrained(){
		JOptionPane.showMessageDialog(null, "System is fully constrained");
	}
	
	private static void warnIn(){
		JOptionPane.showMessageDialog(null, "Invalid Input Parameters");
	}
}
