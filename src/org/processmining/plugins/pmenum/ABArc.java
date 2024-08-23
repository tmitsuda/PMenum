package org.processmining.plugins.pmenum;

public class ABArc extends Arc {
	
	private double maxDependency;
	private double minDependency;
	private double relativeToBest;
	
	public ABArc(int source, int target, double dependency, double relativeToBest) {
		
		super(source, target, 0);
		this.maxDependency = dependency;
		this.minDependency = -1;
		this.relativeToBest = relativeToBest;
		
	}
	
	public ABArc(int source, int target, double maxDependency, double minDependency, double relativeToBest) {
		
		super(source, target, 0);
		this.maxDependency = maxDependency;
		this.minDependency = minDependency;
		this.relativeToBest = relativeToBest;
		
	}
	
	@Override
	public double getMaxDependency() {
		return maxDependency;
	}
	
	@Override
	public double getMinDependency() {
		return minDependency;
	}
	
	@Override
	public double getRelativeToBest() {
		return relativeToBest;
	}
	
	@Override
	public void printInfo() {
		System.out.println(String.valueOf(source) + ", " + String.valueOf(target) + ", 0, " + String.valueOf(maxDependency)+ ", " + String.valueOf(relativeToBest));
	}

}
