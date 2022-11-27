package br.unb.cic.algorithms.matrices.impl;

import java.util.List;

import br.unb.cic.algorithms.matrices.MatrixProduct;
import br.unb.cic.algorithms.matrices.Metrics;
import br.unb.cic.algorithms.util.Matrices;

public class RecursiveWithArrayCopy implements MatrixProduct {
	
	
	private Metrics metrics;
	
	public RecursiveWithArrayCopy() {
		metrics = new Metrics();
	}

	@Override
	public int[][] multiply(int[][] A, int[][] B) {
		metrics.incRecursive();
		// preconditions
		if (!Matrices.isSquare(A) || !Matrices.isSquare(B)) {
			throw new IllegalArgumentException("A and B should be square matrices");
		}
		if (A.length != B.length) {
			throw new IllegalArgumentException("A and B should have the same size");
		}
		int n = A.length;
		if (n == 1) {
			metrics.incAlloc();
			int[][] C = new int[1][1];
			C[0][0] = A[0][0] * B[0][0];
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

			int[][] C11 = Matrices.add(multiply(A11, B11), multiply(A12, B21)); metrics.incAlloc();
			int[][] C12 = Matrices.add(multiply(A11, B12), multiply(A12, B22)); metrics.incAlloc();
			int[][] C21 = Matrices.add(multiply(A21, B11), multiply(A22, B21)); metrics.incAlloc();
			int[][] C22 = Matrices.add(multiply(A21, B12), multiply(A22, B22)); metrics.incAlloc();

			return Matrices.join4(C11, C12, C21, C22);
		}
	}

	@Override
	public Metrics getMetrics() {
		return metrics;
	}

}
