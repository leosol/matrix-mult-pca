package br.unb.cic.algorithms.matrices.impl;

import java.util.List;

import br.unb.cic.algorithms.matrices.MatrixProduct;
import br.unb.cic.algorithms.matrices.Metrics;
import br.unb.cic.algorithms.util.Matrices;

public class StrassenOptimized implements MatrixProduct {
	
private Metrics metrics;
	
	private StandardProduct stdProduct;
	
	public StrassenOptimized() {
		this.metrics = new Metrics();
		this.stdProduct = new StandardProduct(this.metrics);
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
		metrics.incRecursive();
		int n = A.length;
		if (n <= (2<<1)) {
			metrics.incAlloc();
			int[][] C = this.stdProduct.multiply(A, B);
			return C;
		} else {
			List<int[][]> AParts = Matrices.splitIn4(A); metrics.incAllocBy(4);
			List<int[][]> BParts = Matrices.splitIn4(B); metrics.incAllocBy(4);
			int[][] A11 = AParts.get(0);
			int[][] A12 = AParts.get(1);
			int[][] A21 = AParts.get(2);
			int[][] A22 = AParts.get(3);

			int[][] B11 = BParts.get(0);
			int[][] B12 = BParts.get(1);
			int[][] B21 = BParts.get(2);
			int[][] B22 = BParts.get(3);

			int[][] S01 = Matrices.sub(B12, B22); metrics.incAlloc();
			int[][] S02 = Matrices.add(A11, A12); metrics.incAlloc();
			int[][] S03 = Matrices.add(A21, A22); metrics.incAlloc();
			int[][] S04 = Matrices.sub(B21, B11); metrics.incAlloc();
			int[][] S05 = Matrices.add(A11, A22); metrics.incAlloc();
			int[][] S06 = Matrices.add(B11, B22); metrics.incAlloc();
			int[][] S07 = Matrices.sub(A12, A22); metrics.incAlloc();
			int[][] S08 = Matrices.add(B21, B22); metrics.incAlloc();
			int[][] S09 = Matrices.sub(A11, A21); metrics.incAlloc();
			int[][] S10 = Matrices.add(B11, B12); metrics.incAlloc();

			int[][] P1 = multiply(A11, S01);
			int[][] P2 = multiply(S02, B22);
			int[][] P3 = multiply(S03, B11);
			int[][] P4 = multiply(A22, S04);
			int[][] P5 = multiply(S05, S06);
			int[][] P6 = multiply(S07, S08);
			int[][] P7 = multiply(S09, S10);

			int[][] C11 = Matrices.add(Matrices.sub(Matrices.add(P5, P4), P2), P6); metrics.incAllocBy(3);
			int[][] C12 = Matrices.add(P1, P2); metrics.incAlloc();
			int[][] C21 = Matrices.add(P3, P4); metrics.incAlloc();
			int[][] C22 = Matrices.sub(Matrices.add(P5, P1), Matrices.add(P3, P7)); metrics.incAllocBy(3);

			metrics.incAlloc();
			return Matrices.join4(C11, C12, C21, C22);
		}
	}

	@Override
	public Metrics getMetrics() {
		return metrics;
	}

}
