package br.unb.cic.algorithms;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.unb.cic.algorithms.matrices.MatrixProduct;
import br.unb.cic.algorithms.matrices.Metrics;

public class Benchmark {

	private int nStart;
	private int nEnd;
	private Map<String, MatrixProduct> algorithms = new LinkedHashMap<String, MatrixProduct>();
	private Map<Integer, int[][][]> testData = new LinkedHashMap<Integer, int[][][]>();
	private List<BenchmarkResult> results = new LinkedList<Benchmark.BenchmarkResult>();

	public Benchmark(int nStart, int nEnd) {
		this.nStart = nStart;
		this.nEnd = nEnd;
	}
	
	public void run() {
		for (Map.Entry<Integer, int[][][]> testData : this.testData.entrySet()) {
			int n = testData.getKey();
			int[][][] testArray = testData.getValue();
			int[][] A = testArray[0];
			int[][] B = testArray[1];
			
			for (Map.Entry<String, MatrixProduct> algorithmEntry : this.algorithms.entrySet()) {
				String name = algorithmEntry.getKey();
				System.out.println("Running: "+name+" n "+n);
				MatrixProduct algorithm = algorithmEntry.getValue();
				algorithm.getMetrics().start();
				algorithm.multiply(A, B);
				algorithm.getMetrics().end();
				BenchmarkResult results = new BenchmarkResult(name, algorithm, n, (Metrics)algorithm.getMetrics().clone());
				this.results.add(results);
			}
		}
	}
	
	public List<BenchmarkResult> getResults() {
		return results;
	}

	public void addAlgorithm(String name, MatrixProduct algo) {
		algorithms.put(name, algo);
	}

	public void buildRandomTestData() {
		for (int i = nStart; i < nEnd; i++) {
			int[][][] testDatai = buildTestMatrix(i);
			testData.put(new Integer(i), testDatai);
		}
		System.out.println("Test data complete!");
	}

	private int[][][] buildTestMatrix(int n) {
		int matrixSize = 2 << n;
		int seed = (int) (Math.random() * 1000);
		int[][] A = new int[matrixSize][matrixSize];
		int[][] B = new int[matrixSize][matrixSize];
		for (int y = 0; y < A.length; y++) {
			for (int x = 0; x < A[0].length; x++) {
				A[y][x] = Integer.parseInt(y + "" + x);
				B[y][x] = seed * (y / (x+1));
				seed = -1 * seed + y * x;
			}
		}
		return new int[][][] { A, B };
	}

	public static class BenchmarkResult {
		private String name;
		private MatrixProduct algorithm;
		private int n;
		private Metrics results;

		public BenchmarkResult(String name, MatrixProduct algorithm, int n, Metrics results) {
			this.name = name;
			this.algorithm = algorithm;
			this.n = n;
			this.results = results;
		}

		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public MatrixProduct getAlgorithm() {
			return algorithm;
		}

		public void setAlgorithm(MatrixProduct algorithm) {
			this.algorithm = algorithm;
		}

		public int getN() {
			return n;
		}

		public void setN(int n) {
			this.n = n;
		}

		public Metrics getResults() {
			return results;
		}

		public void setResults(Metrics results) {
			this.results = results;
		}
	}

}
