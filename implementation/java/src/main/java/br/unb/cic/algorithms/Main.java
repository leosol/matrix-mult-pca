package br.unb.cic.algorithms;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.List;

import br.unb.cic.algorithms.Benchmark.BenchmarkResult;
import br.unb.cic.algorithms.matrices.impl.RecursiveWithArrayCopy;
import br.unb.cic.algorithms.matrices.impl.RecursiveWithIndexMath;
import br.unb.cic.algorithms.matrices.impl.StandardProduct;
import br.unb.cic.algorithms.matrices.impl.StrassenOptimized;
import br.unb.cic.algorithms.matrices.impl.StrassenWithArrayCopy;

public class Main {

	public static void main(String[] args) throws IOException {

		Benchmark benchmark = new Benchmark(1, 8);
		benchmark.addAlgorithm("Standard Product", new StandardProduct());
		//benchmark.addAlgorithm("Recursive Array Copy", new RecursiveWithArrayCopy());
		benchmark.addAlgorithm("Recursive Indexed", new RecursiveWithIndexMath());
		//benchmark.addAlgorithm("Strassen Array Copy", new StrassenWithArrayCopy());
		benchmark.addAlgorithm("Strassen Optimized", new StrassenOptimized());
		benchmark.buildRandomTestData();
		benchmark.run();
		List<BenchmarkResult> results = benchmark.getResults();
		BenchmarkChart chart1 = new BenchmarkChart(BenchmarkChart.MODE_CPU_TIME, false, false, results);
		BenchmarkChart chart2 = new BenchmarkChart(BenchmarkChart.MODE_ALLOC_COUNT, false, false, results);
		BenchmarkChart chart3 = new BenchmarkChart(BenchmarkChart.MODE_RECURSION_COUNT, false, false, results);

		EventQueue.invokeLater(() -> {
			chart1.setVisible(true);
			chart2.setVisible(true);
			chart3.setVisible(true);
		});

	}
}
