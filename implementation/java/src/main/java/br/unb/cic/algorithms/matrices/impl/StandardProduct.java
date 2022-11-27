package br.unb.cic.algorithms.matrices.impl;

import br.unb.cic.algorithms.matrices.MatrixProduct;
import br.unb.cic.algorithms.matrices.Metrics;
import br.unb.cic.algorithms.util.Matrices;

public class StandardProduct implements MatrixProduct {
	
	private Metrics metrics;
	
	public StandardProduct() {
		this.metrics = new Metrics();
	}
	
	public StandardProduct(Metrics metrics) {
		this.metrics = metrics;
	}

	@Override
	public int[][] multiply(int[][] A, int[][] B) {
		metrics.incAlloc();
		return Matrices.multiply(A, B);
	}

	@Override
	public Metrics getMetrics() {
		return this.metrics;
	}

}
