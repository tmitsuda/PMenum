package org.processmining.plugins.pmenum;

public class L1LArc extends Arc {
	
	private double dependency;
	
	public L1LArc(int source, double dependency) {
		
		super(source, source, 1);
		this.dependency = dependency;
		
	}
	
	@Override
	public double getMaxDependency() {
		return dependency;
	}
	
	@Override
	public void printInfo() {
		System.out.println(String.valueOf(source) + ", " + String.valueOf(target) + ", 1, " + String.valueOf(dependency));
	}

}
