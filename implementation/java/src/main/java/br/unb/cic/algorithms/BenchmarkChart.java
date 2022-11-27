package br.unb.cic.algorithms;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import br.unb.cic.algorithms.Benchmark.BenchmarkResult;

public class BenchmarkChart extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final int MODE_CPU_TIME = 1;
	public static final int MODE_ALLOC_COUNT = 2;
	public static final int MODE_RECURSION_COUNT = 3;

	private boolean logScale;
	private boolean referenceScale;
	private int mode;
	private List<BenchmarkResult> benchmarkResults;
	private int lowestN = Integer.MAX_VALUE;
	private int highestN = Integer.MIN_VALUE;

	public BenchmarkChart(int mode, boolean logScale, boolean referenceScale, List<BenchmarkResult> results) {
		this.mode = mode;
		this.logScale = logScale;
		this.referenceScale = referenceScale;
		this.benchmarkResults = results;
		initUI();
	}

	private void initUI() {
		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		try {
			ChartUtils.saveChartAsPNG(new File(
					"C:\\Users\\leoso\\Google Drive\\1.Leandro\\2.Estudos\\Mestrado\\Unb Especial 2022-3\\Trabalho 1\\"+getPngName()),
					chart, 900, 800);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		add(chartPanel);
		pack();
		setTitle(getChartTitle());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private String getChartTitle() {
		String options = "Logarithmic: " + this.logScale + ", factor of standard: " + this.referenceScale;
		switch (this.mode) {
		case MODE_CPU_TIME:
			return "CPU Time (" + options + ")";
		case MODE_ALLOC_COUNT:
			return "Allocations (" + options + ")";
		case MODE_RECURSION_COUNT:
			return "Recursive calls (" + options + ")";
		default:
			throw new IllegalStateException("Unrecognized mode");
		}
	}
	
	private String getPngName() {
		String modeStr = "";
		switch (this.mode) {
		case MODE_CPU_TIME:
			modeStr = "CPU-TIME";
			break;
		case MODE_ALLOC_COUNT:
			modeStr = "MEM-ALLOCATIONS";
			break;
		case MODE_RECURSION_COUNT:
			modeStr = "RECURSIVE-CALLS";
			break;
		default:
			throw new IllegalStateException("Unrecognized mode");
		}
		String optionsStr = "LOG-" + this.logScale + "_FACTOR_STD-" + this.referenceScale;
		String pngName = "From_"+this.lowestN+"_to_"+this.highestN+"_"+modeStr+"_"+optionsStr+".png";
		return pngName;
	}

	private String getXChartTitle() {
		return "N";
	}

	private String getYChartTitle() {
		switch (this.mode) {
		case MODE_CPU_TIME:
			return "CPU Time";
		case MODE_ALLOC_COUNT:
			return "Allocations";
		case MODE_RECURSION_COUNT:
			return "Recursive calls";
		default:
			throw new IllegalStateException("Unrecognized mode");
		}
	}

	private XYDataset createDataset() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		Map<String, XYSeries> namedSeries = new LinkedHashMap<String, XYSeries>();
		for (BenchmarkResult benchmarkResult : benchmarkResults) {
			if (lowestN > benchmarkResult.getN()) {
				lowestN = benchmarkResult.getN();
			}
			if (highestN < benchmarkResult.getN()) {
				highestN = benchmarkResult.getN();
			}
			String name = benchmarkResult.getName();
			XYSeries data = namedSeries.get(name);
			if (data == null) {
				data = new XYSeries(name);
				dataset.addSeries(data);
				namedSeries.put(name, data);
			}
			switch (this.mode) {
			case MODE_CPU_TIME:
				data.add(2 << (benchmarkResult.getN()), benchmarkResult.getResults().getElapsedMillis());
				break;
			case MODE_ALLOC_COUNT:
				data.add(2 << (benchmarkResult.getN()), benchmarkResult.getResults().getQtdAlloc());
				break;
			case MODE_RECURSION_COUNT:
				data.add(2 << (benchmarkResult.getN()), benchmarkResult.getResults().getQtdRecursive());
				break;
			default:
				throw new IllegalStateException("Unrecognized mode");
			}
		}
		return dataset;
	}

	private JFreeChart createChart(final XYDataset dataset) {

		JFreeChart chart = ChartFactory.createXYLineChart(getChartTitle(), getXChartTitle(), getYChartTitle(), dataset,
				PlotOrientation.VERTICAL, true, true, false);

		XYPlot plot = chart.getXYPlot();

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		renderer.setSeriesPaint(1, Color.BLUE);
		renderer.setSeriesStroke(1, new BasicStroke(2.0f));
		renderer.setSeriesPaint(2, Color.GREEN);
		renderer.setSeriesStroke(2, new BasicStroke(2.0f));
		renderer.setSeriesPaint(3, Color.DARK_GRAY);
		renderer.setSeriesStroke(3, new BasicStroke(2.0f));
		renderer.setSeriesPaint(4, Color.MAGENTA);
		renderer.setSeriesStroke(4, new BasicStroke(2.0f));

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);

		chart.getLegend().setFrame(BlockBorder.NONE);

		chart.setTitle(new TextTitle(getChartTitle(), new Font("Serif", Font.BOLD, 18)));

		return chart;
	}

}
