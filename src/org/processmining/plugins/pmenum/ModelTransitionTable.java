package org.processmining.plugins.pmenum;

import java.util.ArrayList;
import java.util.TreeSet;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.heuristicsnet.miner.heuristics.HeuristicsMetrics;

public class ModelTransitionTable {
	
	private ArrayList<Double> dependencyList;
	private ArrayList<Double> relativeToBestList;
	private boolean[][] modelTransitionTable;
	private ArrayList<Double[]> modelTransitionList;
	private XLog log;
	
	public ModelTransitionTable(HeuristicsMetrics metrics) {
		
		this.dependencyList = this.getDependencyList(metrics);
		this.relativeToBestList = this.getRelativeToBestList(metrics);
		int dependencySize = this.dependencyList.size();
		int relativeToBestSize = this.relativeToBestList.size();
		this.modelTransitionTable = new boolean[dependencySize][relativeToBestSize];
		for(int i = 0; i < dependencySize; i++) {
			for(int j = 0; j < relativeToBestSize; j++) {
				this.modelTransitionTable[i][j] = true;
			}
		}
		
		this.init(metrics);	
		
	}
	
	public ModelTransitionTable(HeuristicsMetrics metrics, XLog log, ArrayList<Arc> arcList) {
		
		this.log = log;
		
		TreeSet<Double> dependencySet = new TreeSet<Double>();
		TreeSet<Double> relativeToBestSet = new TreeSet<Double>();
		
		for(Arc arc : arcList) {
			Double maxDependency = arc.getMaxDependency();
			Double minDependency = arc.getMinDependency();
			Double relativeToBest = arc.getRelativeToBest();
			if(maxDependency < 1) dependencySet.add(maxDependency);
			if(minDependency > 0) dependencySet.add(minDependency);
			if(relativeToBest < 1) relativeToBestSet.add(relativeToBest);
		}
		
		dependencyList = new ArrayList<Double>(dependencySet);
		relativeToBestList = new ArrayList<Double>(relativeToBestSet);
		int dependencySize = this.dependencyList.size();
		int relativeToBestSize = this.relativeToBestList.size();
		this.modelTransitionTable = new boolean[dependencySize][relativeToBestSize];
		for(int i = 0; i < dependencySize; i++) {
			for(int j = 0; j < relativeToBestSize; j++) {
				this.modelTransitionTable[i][j] = true;
			}
		}
		
		this.init(arcList);	
		
	}
	
	private void init(HeuristicsMetrics metrics) {
		
		for(int i = 0; i < dependencyList.size(); i++) {
			Double dependencyThreshold = this.dependencyList.get(i);
			TreeSet<Double> relativeToBestSet = new TreeSet<Double>();
			for(int x = 0; x < metrics.getEventsNumber(); x++) {
				for(int y = 0; y < metrics.getEventsNumber(); y++) {
					if((metrics.getABdependencyMeasuresAll(x, y) >= dependencyThreshold) 
							&& (metrics.getDirectSuccessionCount(x, y) > 0)) {
						Double bestDependency = metrics.getBestOutputMeasure(x);
						Double dependency = metrics.getABdependencyMeasuresAll(x, y);
						Double relativeToBest = bestDependency - dependency;
						relativeToBestSet.add(relativeToBest);
					}
				}
			}
			for(int j = 0; j < relativeToBestList.size(); j++) {
				Double relativeToBestThreshold = this.relativeToBestList.get(j);
				if(!(relativeToBestSet.contains(relativeToBestThreshold))) {
					this.modelTransitionTable[i][j] = false;
				}
			}
		}
		
		for(int j = 0; j < relativeToBestList.size(); j++) {
			Double relativeToBestThreshold = this.relativeToBestList.get(j);
			TreeSet<Double> dependencySet = new TreeSet<Double>();
			for(int x = 0; x < metrics.getEventsNumber(); x++) {
				for(int y = 0; y < metrics.getEventsNumber(); y++) {
					Double bestDependency = metrics.getBestOutputMeasure(x);
					Double dependency = metrics.getABdependencyMeasuresAll(x, y);
					Double relativeToBest = bestDependency - dependency;
					if((relativeToBest <= relativeToBestThreshold) 
							&& (metrics.getDirectSuccessionCount(x, y) > 0)) {
						dependencySet.add(dependency);
					}
				}
			}
			for(int i = 0; i < dependencyList.size(); i++) {
				Double dependencyThreshold = this.dependencyList.get(i);
				if(!(dependencySet.contains(dependencyThreshold))) {
					this.modelTransitionTable[i][j] = false;
				}
			}
		}
		
	}
	
	private void init(ArrayList<Arc> arcList) {
		
		for(int i = 0; i < dependencyList.size(); i++) {
			Double dependencyThreshold = this.dependencyList.get(i);
			TreeSet<Double> relativeToBestSet = new TreeSet<Double>();
			for(Arc arc : arcList) {
				if((arc.getMaxDependency() >= dependencyThreshold)
						&& (arc.getMinDependency() < dependencyThreshold)) {
					relativeToBestSet.add(arc.getRelativeToBest());
				}
			}
			for(int j = 0; j < relativeToBestList.size(); j++) {
				Double relativeToBestThreshold = this.relativeToBestList.get(j);
				if(!(relativeToBestSet.contains(relativeToBestThreshold))) {
					this.modelTransitionTable[i][j] = false;
				}
			}
		}
		
		for(int j = 0; j < relativeToBestList.size(); j++) {
			Double relativeToBestThreshold = this.relativeToBestList.get(j);
			TreeSet<Double> dependencySet = new TreeSet<Double>();
			for(Arc arc : arcList) {
				if(arc.getRelativeToBest() <= relativeToBestThreshold) {
					dependencySet.add(arc.getMaxDependency());
					dependencySet.add(arc.getMinDependency());
				}
			}
			for(int i = 0; i < dependencyList.size(); i++) {
				Double dependencyThreshold = this.dependencyList.get(i);
				if(!(dependencySet.contains(dependencyThreshold))) {
					this.modelTransitionTable[i][j] = false;
				}
			}
		}
		
		this.modelTransitionList = new ArrayList<Double[]>();
		for(int i = this.dependencyList.size() - 1; i >= 0; i--) {
			for(int j = 0; j < this.relativeToBestList.size(); j++) {
				if(this.modelTransitionTable[i][j]) {
					Double[] thresholds = {this.dependencyList.get(i), this.relativeToBestList.get(j)};
					modelTransitionList.add(thresholds);
				}
			}
		}
		
	}
	
	public ArrayList<Double> getDependencyList() {
		return this.dependencyList;
	}
	
	public ArrayList<Double> getDependencyList(HeuristicsMetrics metrics) {
		
		TreeSet<Double> dependencySet = new TreeSet<Double>();
		for (int i = 0; i < metrics.getEventsNumber(); i++) {
			for (int j = 0; j < metrics.getEventsNumber(); j++) {
				if(metrics.getDirectSuccessionCount(i, j) > 0) {
					Double dependency = metrics.getABdependencyMeasuresAll(i, j);
					if(dependency >= 0) {
						dependencySet.add(dependency);
					}
				}
			}
		}
		ArrayList<Double> dependencyList = new ArrayList<Double>(dependencySet);
		return dependencyList;
	}
	
	public ArrayList<Double> getRelativeToBestList() {
		return this.relativeToBestList;
	}
	
	public ArrayList<Double> getRelativeToBestList(HeuristicsMetrics metrics) {
		
		TreeSet<Double> relativeToBestSet = new TreeSet<Double>();
		for (int i = 0; i < metrics.getEventsNumber(); i++) {
			Double bestDependency = metrics.getBestOutputMeasure(i);
			for (int j = 0; j < metrics.getEventsNumber(); j++) {
				if(i != j) {
					Double dependency = metrics.getABdependencyMeasuresAll(i, j);
					if(dependency >= 0) {
						relativeToBestSet.add(bestDependency - dependency);
					}
				}
			}
		}
		ArrayList<Double> relativeToBestList = new ArrayList<Double>(relativeToBestSet);
		return relativeToBestList;
	}
	
	
	public void printModelList() {
		
		System.out.println("List of Model generated by Heuristic Miner");
		for(Double[] thresholds : this.modelTransitionList) {
			System.out.println(String.valueOf(thresholds[0]) + ", " + String.valueOf(thresholds[1]));
		}
		System.out.println();
	}
	
	public void printModelTable() {
		
		System.out.println("Table of Model generated by Heuristic Miner");
		System.out.print(',');
		for(int i = this.dependencyList.size() - 1; i >= 0; i--) {
			System.out.print(dependencyList.get(i));
			System.out.print(',');
		}
		System.out.println();
		for(int j = 0; j < this.relativeToBestList.size(); j++) {
			System.out.print(relativeToBestList.get(j));
			System.out.print(',');
			for(int i = this.dependencyList.size() - 1; i >= 0; i--) {
				if(!this.modelTransitionTable[i][j]) {
					System.out.print('x');
				}
				System.out.print(',');
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public XLog getLog() {
		return this.log;
	}
	
	public ArrayList<Double[]> getModelTransitionList(){
		return modelTransitionList;
	}
	
	public boolean[][] getModelTransitionTable(){
		return this.modelTransitionTable;
	}
	
	public String getModelTransitionTableString() {
		StringBuilder data = new StringBuilder();
		data.append(',');
		for(int i = this.dependencyList.size() - 1; i >= 0; i--) {
			data.append(dependencyList.get(i));
			data.append(',');
		}
		data.append('\n');
		for(int j = 0; j < this.relativeToBestList.size(); j++) {
			data.append(relativeToBestList.get(j));
			data.append(',');
			for(int i = this.dependencyList.size() - 1; i >= 0; i--) {
				if(!this.modelTransitionTable[i][j]) {
					data.append('-');
				}
				data.append(',');
			}
			data.append('\n');
		}
		
		return data.toString();
	}
	
	public String getParameterValueListString() {
		
		StringBuilder data = new StringBuilder();
		data.append("dep-TH, rtb-TH\n");
		for(Double[] thresholds : this.modelTransitionList) {
			data.append(String.valueOf(thresholds[0]) + ", " + String.valueOf(thresholds[1]) + "\n");
		}

		return data.toString();
		
	}

}
