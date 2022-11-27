package br.unb.cic.algorithms.matrices;

public interface MatrixProduct {
	
	public int[][] multiply(int[][] A, int[][] B);
	
	public Metrics getMetrics();

}
