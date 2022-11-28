package br.unb.cic.algorithms.matrices.impl;

import br.unb.cic.algorithms.matrices.MatrixProduct;
import br.unb.cic.algorithms.matrices.Metrics;
import br.unb.cic.algorithms.util.Matrices;
import br.unb.cic.algorithms.util.Matrices.MatrixDivision;

public class StrassenWithIndexMath implements MatrixProduct {
	
	private Metrics metrics;
	
	public StrassenWithIndexMath() {
		this.metrics = new Metrics();
	}
	
	@Override
	public int[][] multiply(int[][] A, int[][] B) {
		// preconditions
		if (!Matrices.isSquare(A) || !Matrices.isSquare(B)) {
			throw new IllegalArgumentException("A and B should be square matrices");
		}
		if (A.length != B.length) {
			throw new IllegalArgumentException("A and B should have the same size");
		}
		int n = A.length;
		int[][] C = new int[n][n]; metrics.incAlloc();
		imultiply(A, B, C, 0, 0, 0, 0, 0, 0, n);
		return C;
	}

	private void imultiply(int[][] A, int[][] B, int[][] C, int Ay, int Ax, int By, int Bx, int Cy, int Cx, int n) {
		metrics.incRecursive();
		if (n == 1) {
			C[Cy][Cx] = C[Cy][Cx] + A[Ay][Ax] * B[By][Bx];
		} else {
			MatrixDivision divA = Matrices.splitIn4Indexes(Ay, Ax, n);
			MatrixDivision divB = Matrices.splitIn4Indexes(By, Bx, n);
			MatrixDivision divC = Matrices.splitIn4Indexes(Cy, Cx, n);
			//A11: A, divA.M11y, divA.M11x,
			//A12: A, divA.M12y, divA.M12x
			//A21: A, divA.M21y, divA.M21x
			//A22: A, divA.M22y, divA.M22x
			//B11: B, divB.M11y, divB.M11x,
			//B12: B, divB.M12y, divB.M12x
			//B21: B, divB.M21y, divB.M21x
			//B22: B, divB.M22y, divB.M22x
			//C11: C, divC.M11y, divC.M11x,
			//C12: C, divC.M12y, divC.M12x
			//C21: C, divC.M21y, divC.M21x
			//C22: C, divC.M22y, divC.M22x
			metrics.incAllocBy(10);
			int[][] S01 = Matrices.isub(B, B, divB.M12y, divB.M12x, divB.M22y, divB.M22x, divB.n); //B12 - B22
			int[][] S02 = Matrices.iadd(A, A, divA.M11y, divA.M11x, divA.M12y, divA.M12x, divA.n); //A11 - A12
			int[][] S03 = Matrices.iadd(A, A, divA.M21y, divA.M21x, divA.M22y, divA.M22x, divA.n); //A21 - A22
			int[][] S04 = Matrices.isub(B, B, divB.M21y, divB.M21x, divB.M11y, divB.M11x, divB.n); //B21 - B11
			int[][] S05 = Matrices.iadd(A, A, divA.M11y, divA.M11x, divA.M22y, divA.M22x, divA.n); //A11 - A22
			int[][] S06 = Matrices.iadd(B, B, divB.M11y, divB.M11x, divB.M22y, divB.M22x, divB.n); //B11 - B22
			int[][] S07 = Matrices.isub(A, A, divA.M12y, divA.M12x, divA.M22y, divA.M22x, divA.n); //A12 - A22
			int[][] S08 = Matrices.iadd(B, B, divB.M21y, divB.M21x, divB.M22y, divB.M22x, divB.n); //B21 - B22
			int[][] S09 = Matrices.isub(A, A, divA.M11y, divA.M11x, divA.M21y, divA.M21x, divA.n); //A11 - A21
			int[][] S10 = Matrices.iadd(B, B, divB.M11y, divB.M11x, divB.M12y, divB.M12x, divB.n); //B11 - B12

			metrics.incAllocBy(7);
			int[][] P1 = new int[divA.n][divA.n];
			int[][] P2 = new int[divA.n][divA.n];
			int[][] P3 = new int[divA.n][divA.n];
			int[][] P4 = new int[divA.n][divA.n];
			int[][] P5 = new int[divA.n][divA.n];
			int[][] P6 = new int[divA.n][divA.n];
			int[][] P7 = new int[divA.n][divA.n];
			
			imultiply(A, S01, P1, divA.M11y, divA.M11x, 0, 0, 0, 0, divA.n);// P1 = multiply(A11, S01);
			imultiply(S02, B, P2, 0, 0, divB.M22y, divB.M22x, 0, 0, divA.n);// P2 = multiply(S02, B22);
			imultiply(S03, B, P3, 0, 0, divB.M11y, divB.M11x, 0, 0, divA.n);// P3 = multiply(S03, B11);
			imultiply(A, S04, P4, divA.M22y, divA.M22x, 0, 0, 0, 0, divA.n);// P4 = multiply(A22, S04);
			imultiply(S05, S06, P5, 0, 0, 0, 0, 0, 0, divA.n);// P5 = multiply(S05, S06);
			imultiply(S07, S08, P6, 0, 0, 0, 0, 0, 0, divA.n);// P6 = multiply(S07, S08);
			imultiply(S09, S10, P7, 0, 0, 0, 0, 0, 0, divA.n);// P7 = multiply(S09, S10);
			
			//C11 = C11 + P5 + P4 - P2 + P6
			Matrices.iadd(C, P5, C, divC.M11y, divC.M11x, 0, 0, divC.M11y, divC.M11x, divC.n);
			Matrices.iadd(C, P4, C, divC.M11y, divC.M11x, 0, 0, divC.M11y, divC.M11x, divC.n);
			Matrices.isub(C, P2, C, divC.M11y, divC.M11x, 0, 0, divC.M11y, divC.M11x, divC.n);
			Matrices.iadd(C, P6, C, divC.M11y, divC.M11x, 0, 0, divC.M11y, divC.M11x, divC.n);
			
			//C12 = C12 + P1 + P2
			Matrices.iadd(C, P1, C, divC.M12y, divC.M12x, 0, 0, divC.M12y, divC.M12x, divA.n);
			Matrices.iadd(C, P2, C, divC.M12y, divC.M12x, 0, 0, divC.M12y, divC.M12x, divA.n);
			
			//C21 = C21 + P3 + P4
			Matrices.iadd(C, P3, C, divC.M21y, divC.M21x, 0, 0, divC.M21y, divC.M21x, divA.n);
			Matrices.iadd(C, P4, C, divC.M21y, divC.M21x, 0, 0, divC.M21y, divC.M21x, divA.n);
			
			//C22 = C22 + P5 + P1 - P3 -P7
			Matrices.iadd(C, P5, C, divC.M22y, divC.M22x, 0, 0, divC.M22y, divC.M22x, divA.n);
			Matrices.iadd(C, P1, C, divC.M22y, divC.M22x, 0, 0, divC.M22y, divC.M22x, divA.n);
			Matrices.isub(C, P3, C, divC.M22y, divC.M22x, 0, 0, divC.M22y, divC.M22x, divA.n);
			Matrices.isub(C, P7, C, divC.M22y, divC.M22x, 0, 0, divC.M22y, divC.M22x, divA.n);
		}
	}

	@Override
	public Metrics getMetrics() {
		return metrics;
	}

}
