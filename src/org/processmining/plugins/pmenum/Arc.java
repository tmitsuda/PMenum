package org.processmining.plugins.pmenum;

public class Arc {
	
	protected int source;
	protected int target;
	private int type;
	
	protected Arc(int source, int target, int type) {
		
		this.source = source;
		this.target = target;
		this.type = type;
		
	}
	
	public double getMaxDependency() {
		return 1;
	}
	
	public double getMinDependency() {
		return -1;
	}
	
	public double getRelativeToBest() {
		return 0;
	}
	
	public void printInfo() {
		System.out.println(String.valueOf(source) + ", " + String.valueOf(target) + ", " + String.valueOf(type));
	}

}
