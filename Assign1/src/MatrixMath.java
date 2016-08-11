import Jama.*;
public class MatrixMath {

	public static double [][] Identity(int n) {
		
		double [][] I = new double[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				
				if (i==j) I[i][j] = 1;
				else I[i][j]=0;
				
			}
		}
		
		return I;
	}

	public static double [][] Subtract( double [][] matrix1, double [][] matrix2){
	
		if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length)
			return null; 
		
		double[][] Answer = new double [matrix1.length][matrix1[0].length];
		
		for (int i = 0; i < matrix1.length; i++){
			for (int j = 0; j < matrix1[0].length; j++){
				Answer [i][j] = matrix1[i][j]-matrix2[i][j];
			}
		}
		return Answer;
	}
	
	public static double [] Subtract( double [] matrix1, double [] matrix2){
		
		if (matrix1.length != matrix2.length)
			return null; 
		
		double[] Answer = new double [matrix1.length];
		
		for (int i = 0; i < matrix1.length; i++){
			Answer [i] = matrix1[i]-matrix2[i];
		}
		return Answer;
	}


	public static double [][] Add( double [][] matrix1, double [][] matrix2){
	
		if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length)
			return null;
		
		double[][] Answer = new double [matrix1.length][matrix1[0].length];
		
		for (int i = 0; i < matrix1.length; i++){
			for (int j = 0; j < matrix1[0].length; j++){
				Answer [i][j] = matrix1[i][j]+matrix2[i][j];
			}
		}
		return Answer;
	}

	public static double [] Add( double [] matrix1, double [] matrix2){
		
		if (matrix1.length != matrix2.length)
			return null;
		
		double[] Answer = new double [matrix1.length];
		
		for (int i = 0; i < matrix1.length; i++){
			Answer [i] = matrix1[i]+matrix2[i];
		}
		return Answer;
	}
	
	public static double [][] Multiply(double [][] A, double k) {
		
		int n = A.length;
		int m = A[0].length;
		
		double [][] B = new double [n][m];
		
		for (int i=0; i < n; i++) {
			
			for(int j=0; j < m; j++) {
				
				B[i][j] = k*A[i][j];
			}
		}
		
		return B;
	}
	
	public static double [] Multiply(double [] A, double k) {
		
		int n = A.length;
		double [] B = new double[n];
		
		for (int i = 0; i < n; i++) {
			
			B[i] = k*A[i];
			
		}
		
		return B;
	}
	
	
	
	public static double [] Multiply(double [][] A, double [] k) {
		//2D matrix times 1D
				
		if (A[0].length != k.length) {
			
			return null;
		}
		
		double [] B = new double[A.length];
		
		for (int i = 0; i < A.length; i++) {
			
			for (int j = 0; j < A[0].length; j++ ) {
				
				B[i] += A[i][j]*k[j];
				
			}
		}
		
		return B;
	}
	
	public static double [][] Multiply(double [][] A, double [][] B) {
		//2D matrix times 2D
		int n = A.length;
		int m = A[0].length;
		int m2 = B.length;
		int p = B[0].length;
		
		if (m != m2){
			
			return null;
			
		}
		
		double [][] R = new double[A.length][B[0].length];
		
		for (int i=0; i < n; i++) {
			for (int j=0; j < p; j++) {
				R[i][j] = 0;
				for (int k=0; k < m; k++) {
					R[i][j] += A[i][k]*B[k][j];
				}
			}
		}
		
		return R;
	}
	
	public static double [] Multiply(double [] A, double [] B) {
		//1D times 1D
		
		double [] R = new double[A.length];
		
		for (int i = 0; i < A.length; i++) {
			R[i] = A[i]*B[i];
		}
		
		return R;
	}
	
	public static double [][] Cofactor(double [][] A) {
		
		int n = A.length;
		int m = A[0].length;
		
		//Assume square
		if (n != m) {
			return null;
		}
		
		double [][] B = new double[n][m];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j ++) {
				
				double [][] Bp = new double[n-1][m-1];
				
				for (int r = 0; r < n; r++) {
					for (int s = 0; s < m; s++) {
						
						if ((r < i) && (s < j)) {
							
							Bp[r][s] = A[r][s];
						}
						else if ((r < i) && (s > j)) {
							
							Bp[r][s-1] = A[r][s];
						}
						else if ((r > i) && (s < j)) {
							
							Bp[r-1][s] = A[r][s];
						}
						else if ((r > i) && (s > j)) {
							
							Bp[r-1][s-1] = A[r][s];
						}
						// else do nothing
					}
				}
				
				if ((i+j)%2 == 0 ) { // 1
					 
					B[i][j] = det(Bp);
				}
				else { // -1
					
					B[i][j] = (-1)*(det(Bp));
				}	
			}
		}
		
		return B; 
	}
	
	public static double [][] Transpose(double [][] A) {
		
		int n = A.length;
		int m = A[0].length;
		
		double [][] B = new double[n][m];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				
				B[j][i] = A[i][j];
			}
		}
		
		return B;
	}
	
	public static double [][] Invert(double [][] A) {
		// 2D matrix inversion
		
		int n = A.length;
		int m = A[0].length;
		
		if (n != m) {
			return null;
		}
		
		double detA = det(A);
		
		if (detA == 0) {
			return null;
		}
		
		double [][] C = new double[n][m];
		
		C = Cofactor(A);
		C = Transpose(C);
		
		return Multiply(C, 1/detA);
	}
	
	public static double[] CramersSolve(double[][] constMat, double[]bMat){
		double constDet = det(constMat);
		double[]answer=new double[constMat.length];
		for (int i=0; i<answer.length; i++){
			double[][] a=new double[constMat.length][constMat.length];
			for(int j=0; j<constMat.length; j++){
				if(j==i){
					for(int k=0; k<constMat.length;k++){
						a[j][k]=constMat[j][k];
					}
				}
				else{
					for(int k=0; k<constMat.length; k++){
						a[j][k]=bMat[k];
					}
				}
			}
			answer[i]=det(a)/constDet;
		}
		return answer;
	}
	
	private static double det(double [][] a){
		double answer = 0.0;
		if (a.length>2){
			for(int i=0; i<a.length; i++){
				if (a[0][i] != 0.0){
					double[][]submatrix = new double[a.length-1][a.length-1];
					for(int j=0; j<submatrix.length; j++){
						for( int k=0; k<submatrix.length; k++){
							if(k>=i){
								submatrix[j][k]=a[j+1][k+1];
							}
							else{
								submatrix[j][k]=a[j+1][k];
							}
						}
						if(i%2==0){
							answer+=a[0][i]*det(submatrix);
						}
						else{
							answer-=a[0][i]*det(submatrix);
						}
					}
				}
			}	
			return answer;
		}
		return (a[0][0]*a[1][1]-a[0][1]*a[1][0]);
	}
	
	public static double Determinant(double [][] A) {
		
		double B = det(A);
				
		return B;
	}
		
	public static double LUDDet(double [][] A) {
		
		Matrix m = new Matrix(A);
		LUDecomposition x = new LUDecomposition(m);

		Matrix temp = x.getU();
		double [][] tempA = temp.getArrayCopy();
		double det = 1;
		for (int i = 0; i < tempA.length; i++){
			det *= tempA[i][i];
		}
		
		return det;
	}
}
