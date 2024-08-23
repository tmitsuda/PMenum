package org.processmining.plugins.pmenum;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.deckfour.xes.classification.XEventAndClassifier;
import org.deckfour.xes.classification.XEventAttributeClassifier;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventLifeTransClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.heuristics.impl.HNSubSet;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.FlexibleHeuristicsMiner;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.FlexibleHeuristicsMinerPlugin;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.settings.HeuristicsMinerSettings;

public class PMenumMiner extends FlexibleHeuristicsMiner {
	
	private ArrayList<Arc> arcList;
	private ModelTransitionTable modelTransitionTable;
	
	public PMenumMiner(PluginContext context, XLog log) {
		super(context, log);
	}
	
	public PMenumMiner(PluginContext context, XLog log, XLogInfo logInfo) {
		super(context, log, logInfo);
	}
	
	public PMenumMiner(PluginContext context, XLog log, HeuristicsMinerSettings settings) {
		super(context, log, settings);
	}
	
	public PMenumMiner(PluginContext context, XLog log, XLogInfo logInfo, HeuristicsMinerSettings settings) {
		super(context, log, logInfo, settings);
	}
	
	public HeuristicsNet mine() {
		
		this.settings.setL1lThreshold(1);
		
		HeuristicsNet net = super.mine();
		
		arcList = new ArrayList<Arc>();
		
		//add L1L Arcs into ArcList
		for(int i = 0; i < metrics.getEventsNumber(); i++) {
			if(metrics.getDirectSuccessionCount(i, i) >= 1) {
				double dependency = metrics.getL1LdependencyMeasuresAll(i);
				arcList.add(new L1LArc(i, dependency));
			}
		}
		
		for(int i = 0; i < metrics.getEventsNumber(); i++) {
			for(int j = 0; j < metrics.getEventsNumber(); j++) {
				
				double maxL1LDependency = Math.max(
						metrics.getL1LdependencyMeasuresAll(i), 
						metrics.getL1LdependencyMeasuresAll(j));
				double L2LDependency = metrics.getL2LdependencyMeasuresAll(i, j);
				
				//add L2L Arcs into ArcList
				if(i > j) {
					if(L2LDependency > maxL1LDependency) {
						arcList.add(new L2LArc(i, j, L2LDependency, maxL1LDependency));
						arcList.add(new L2LArc(j, i, L2LDependency, maxL1LDependency));
					}
				}
				
				//add AB Arcs into ArcList
				if(i != j) {
					double ABDependency = metrics.getABdependencyMeasuresAll(i, j);
					double relativeToBest = metrics.getBestOutputMeasure(i) - ABDependency;
					
					if((metrics.getDirectSuccessionCount(i, j) >= 1)
							&& (ABDependency > 0)) {	//L2Lthreshold=0‚Ì‚Æ‚«L2L‚Ì”»’è‚ªƒoƒO‚é‚Ì‚Ådependency=0‚ÍNG
						if(L2LDependency > maxL1LDependency) {
							//case L2L Arc exists
							double minDependency = Math.min(ABDependency, maxL1LDependency);
							if(minDependency > 0) {
								arcList.add(new ABArc(i, j, minDependency, relativeToBest));
							}
							if(ABDependency > L2LDependency) {
								arcList.add(new ABArc(i, j, ABDependency, L2LDependency, relativeToBest));
							}
						}else {
							//case L2L Arc doesn't exist
							arcList.add(new ABArc(i, j, ABDependency, relativeToBest));
						}	
					}				
				}
			}
		}
		
		//this.modelTransitionTable = new ModelTransitionTable(super.metrics);
		this.modelTransitionTable = new ModelTransitionTable(super.metrics, log, arcList);
		
		return net;
	}
	
	public void printModelTransition() {
		this.modelTransitionTable.printModelList();
		this.modelTransitionTable.printModelTable();
	}
	
	public void printArcList() {
		System.out.println("List of Arcs exist in Heuristic Model");
		for(Arc arc : arcList) {
			arc.printInfo();
		}
		System.out.println();
	}
	
	public ModelTransitionTable getModelTable() {
		return this.modelTransitionTable;
	}
	
	public void outputAllModels() {
		System.out.println("All Models mined By Model Transition Table");
		
		XLog log = this.log;
		
		XEventClassifier defaultClassifier = null;
		if (log.getClassifiers().isEmpty()) {
			XEventClassifier nameCl = new XEventNameClassifier();
            XEventClassifier lifeTransCl = new XEventLifeTransClassifier();
            XEventAttributeClassifier attrClass = new XEventAndClassifier(nameCl, lifeTransCl);
            defaultClassifier = attrClass;
		} else {
			defaultClassifier = log.getClassifiers().get(0);
		}
		
		XLogInfo loginfo = new XLogInfoImpl(log, defaultClassifier, log.getClassifiers());
		
		ArrayList<Double[]> modelTransitionList = this.modelTransitionTable.getModelTransitionList();
		
		FileWriter file;
		try {
			file = new FileWriter("C:\\prog\\uniqueModelList.csv");
			PrintWriter pw = new PrintWriter(new BufferedWriter(file));
			
			for(Double[] thresholds : modelTransitionList) {
				HeuristicsMinerSettings minerSettings = new HeuristicsMinerSettings();
				minerSettings.setClassifier(defaultClassifier);
				minerSettings.setDependencyThreshold(thresholds[0]);
				minerSettings.setL1lThreshold(thresholds[0]);
				minerSettings.setL2lThreshold(thresholds[0]);
				minerSettings.setRelativeToBestThreshold(thresholds[1]);
				minerSettings.setAndThreshold(Double.POSITIVE_INFINITY);
				minerSettings.setUseAllConnectedHeuristics(false);
				minerSettings.setCheckBestAgainstL2L(false);
				
				HeuristicsNet net = FlexibleHeuristicsMinerPlugin.run(context, log, minerSettings, loginfo);
				
				pw.print(thresholds[0].toString() + "," + thresholds[1].toString() + ",");
				for(int i = 0; i < net.size(); i++) {
					HNSubSet connection = net.getAllElementsOutputSet(i);
					pw.print(connection.toString().replace(',', '.'));
				}
				
				pw.println();
			}
			pw.close();
			
			if(true) {	//whether output duplicate models
				file = new FileWriter("C:\\prog\\duplicateModelList.csv");
				pw = new PrintWriter(new BufferedWriter(file));
				
				ArrayList<Double> dependencyList = this.modelTransitionTable.getDependencyList();
				ArrayList<Double> relativeToBestList = this.modelTransitionTable.getRelativeToBestList();
				boolean[][] modelTransitionTable = this.modelTransitionTable.getModelTransitionTable();
				
				for(int i = dependencyList.size() - 1; i >= 0; i--) {
					for(int j = 0; j < relativeToBestList.size(); j++) {
						if(!modelTransitionTable[i][j]) {
							Double[] thresholds = {dependencyList.get(i), relativeToBestList.get(j)};
							HeuristicsMinerSettings minerSettings = new HeuristicsMinerSettings();
							minerSettings.setClassifier(defaultClassifier);
							minerSettings.setDependencyThreshold(thresholds[0]);
							minerSettings.setL1lThreshold(thresholds[0]);
							minerSettings.setL2lThreshold(thresholds[0]);
							minerSettings.setRelativeToBestThreshold(thresholds[1]);
							minerSettings.setAndThreshold(Double.POSITIVE_INFINITY);
							minerSettings.setUseAllConnectedHeuristics(false);
							minerSettings.setCheckBestAgainstL2L(false);
							
							HeuristicsNet net = FlexibleHeuristicsMinerPlugin.run(context, log, minerSettings, loginfo);
							
							pw.print(thresholds[0].toString() + "," + thresholds[1].toString() + ",");
							for(int k = 0; k < net.size(); k++) {
								HNSubSet connection = net.getAllElementsOutputSet(k);
								pw.print(connection.toString().replace(',', '.'));
							}
							
							pw.println();
						}
					}
				}
				
				pw.close();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
