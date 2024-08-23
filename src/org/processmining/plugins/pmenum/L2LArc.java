package org.processmining.plugins.pmenum;

public class L2LArc extends Arc {
	
	private double maxDependency;
	private double minDependency;
	
	public L2LArc(int source, int target, double maxDependency, double minDependency) {
		
		super(source, target, 2);
		
		this.maxDependency = maxDependency;
		this.minDependency = minDependency;
		
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
	public void printInfo() {
		System.out.println(String.valueOf(source) + ", " + String.valueOf(target) + ", 2, " + String.valueOf(maxDependency));
	}

}
