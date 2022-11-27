package br.unb.cic.algorithms.matrices;

public class Metrics implements Cloneable{

	private long qtdAlloc;
	private long qtdRecursive;
	private long startTime;
	private long endTime;

	public Metrics() {

	}

	public void incAllocBy(int factor) {
		qtdAlloc = qtdAlloc + factor;
	}

	public void incAlloc() {
		qtdAlloc = qtdAlloc + 1;
	}

	public void incRecursive() {
		qtdRecursive = qtdRecursive + 1;
	}

	public void reset() {
		qtdAlloc = qtdRecursive = startTime = endTime = 0;
	}

	public void start() {
		reset();
		this.startTime = System.nanoTime();
	}

	public void end() {
		this.endTime = System.nanoTime();
	}

	public double getElapsedSeconds() {
		return (this.endTime - this.startTime) / 1_000_000_000;
	}
	
	public double getElapsedMillis() {
		return (this.endTime - this.startTime) / 1_000_000;
	}
	
	public long getQtdAlloc() {
		return qtdAlloc;
	}

	public long getQtdRecursive() {
		return qtdRecursive;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	@Override
	public Object clone() {
		Metrics m = new Metrics();
		m.endTime = this.endTime;
		m.startTime = this.startTime;
		m.qtdAlloc = this.qtdAlloc;
		m.qtdRecursive = this.qtdRecursive;
		return m;
	}
	
}
