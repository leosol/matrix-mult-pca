package br.unb.cic.algorithms.matrices.impl;


import br.unb.cic.algorithms.matrices.MatrixProduct;
import br.unb.cic.algorithms.matrices.Metrics;
import br.unb.cic.algorithms.util.Matrices;
import br.unb.cic.algorithms.util.Matrices.MatrixDivision;

public class RecursiveWithIndexMath implements MatrixProduct {
	
	private Metrics metrics;
	
	public RecursiveWithIndexMath() {
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

	public void imultiply(int[][] A, int[][] B, int[][] C, int Ay, int Ax, int By, int Bx, int Cy, int Cx, int n) {
		metrics.incRecursive();
		if (n == 1) {
			C[Cy][Cx] = C[Cy][Cx] + A[Ay][Ax] * B[By][Bx];
		} else {
			MatrixDivision divA = Matrices.splitIn4Indexes(Ay, Ax, n);
			MatrixDivision divB = Matrices.splitIn4Indexes(By, Bx, n);
			MatrixDivision divC = Matrices.splitIn4Indexes(Cy, Cx, n);

			// @formatter:off
			imultiply(A, B, C, divA.M11y, divA.M11x, divB.M11y, divB.M11x, divC.M11y, divC.M11x, divC.n); //C11 <= A11*B11 
			imultiply(A, B, C, divA.M12y, divA.M12x, divB.M21y, divB.M21x, divC.M11y, divC.M11x, divC.n); //C11 <= A12*B21
			
			imultiply(A, B, C, divA.M11y, divA.M11x, divB.M12y, divB.M12x, divC.M12y, divC.M12x, divC.n); //C12 <= A11, B12
			imultiply(A, B, C, divA.M12y, divA.M12x, divB.M22y, divB.M22x, divC.M12y, divC.M12x, divC.n); //C12 <= A12, B22
			
			imultiply(A, B, C, divA.M21y, divA.M21x, divB.M11y, divB.M11x, divC.M21y, divC.M21x, divC.n); //C21 <= A21, B11
			imultiply(A, B, C, divA.M22y, divA.M22x, divB.M21y, divB.M21x, divC.M21y, divC.M21x, divC.n); //C21 <= A22, B21
			
			imultiply(A, B, C, divA.M21y, divA.M21x, divB.M12y, divB.M12x, divC.M22y, divC.M22x, divC.n); //C22 <= A21*B12
			imultiply(A, B, C, divA.M22y, divA.M22x, divB.M22y, divB.M22x, divC.M22y, divC.M22x, divC.n); //C22 <= A22*B22
			// @formatter:on
		}

	}

	@Override
	public Metrics getMetrics() {
		return metrics;
	}

}
