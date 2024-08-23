package org.processmining.plugins.pmenum;

import java.util.Arrays;

import org.deckfour.xes.classification.XEventAndClassifier;
import org.deckfour.xes.classification.XEventAttributeClassifier;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventLifeTransClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.settings.HeuristicsMinerSettings;


@Plugin(name = "Generate Model Transition Table for Heuristic Miner",
		level = PluginLevel.Regular,
		parameterLabels = {"Log", "Settings", "Log Info"},
		returnLabels = {"Model Transition Table"},
		returnTypes = {ModelTransitionTable.class},
		userAccessible = true,
		categories = { PluginCategory.Discovery },
		help = "Discover all Heuristics Net.")
public class PMenumMinerPlugin {
	//TODO - Add documentation
	
	//TODO - Add a help
	@UITopiaVariant(affiliation = "Osaka University", author = "Takeharu MITSUDA", email = "t-mitsuda@ist.osaka-u.ac.jp", website = "", pack = "PMenum")
	@PluginVariant(variantLabel = "Mine Heuristics Net using Wizard (1)", requiredParameterLabels = { 0 })
	public static ModelTransitionTable run(UIPluginContext context, XLog log) {
		//TODO - Build different plug-ins that already receive log infos!!!
		//Note that, the default classifier are been used at the moment
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
		
		HeuristicsMinerSettings settings = new HeuristicsMinerSettings();
        Object[] optionsArr = loginfo.getEventClassifiers().toArray();
        Arrays.sort(optionsArr);
		settings.setClassifier((XEventClassifier) optionsArr[0]);
		
		/* Discover all pairs of threshold which generate respective models */
		PMenumMiner discover = new PMenumMiner(context, log, loginfo, settings);
		HeuristicsNet net = discover.mine();
		discover.printModelTransition();
		discover.printArcList();
		if(false) {		// DEBUG
			discover.outputAllModels();
		}		
		
		return discover.getModelTable();
	}
	
	/*
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "A.J.M.M. Weijters", email = "a.j.m.m.weijters@tue.nl", website = "http://is.tm.tue.nl/staff/aweijters")
	@PluginVariant(variantLabel = "Mine Heuristics Net using Wizard (2)", requiredParameterLabels = { 0, 2 })
	public static HeuristicsNet run(UIPluginContext context, XLog log, XLogInfo logInfo) {
		//TODO - Build different plug-ins that already receive log infos!!!
		//Note that, the default classifier are been used at the moment
		
		ParametersPanel parameters = new ParametersPanel(LogUtility.getEventClassifiers(log));
		parameters.removeAndThreshold();
	
		InteractionResult result = context.showConfiguration("Heuristics Miner Parameters", parameters);
		if (result.equals(InteractionResult.CANCEL)) {
			context.getFutureResult(0).cancel(true);
		}
		return run(context, log, parameters.getSettings(), logInfo);
	}
	
	@PluginVariant(variantLabel = "Mine Heuristics Net using Default Settings (1)", requiredParameterLabels = { 0 })
	public static HeuristicsNet run(PluginContext context, XLog log) {

		//XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
		FlexibleHeuristicsMiner fhm = new FlexibleHeuristicsMiner(context, log, new HeuristicsMinerSettings());
		return fhm.mine();
	}
	
	@PluginVariant(variantLabel = "Mine Heuristics Net using Default Settings (2)", requiredParameterLabels = { 0, 2 })
	public static HeuristicsNet run(PluginContext context, XLog log, XLogInfo logInfo) {

		FlexibleHeuristicsMiner fhm = new FlexibleHeuristicsMiner(context, log, logInfo);
		return fhm.mine();
	}
	
	@PluginVariant(variantLabel = "Mine Heuristics Net using Given Settings (1)", requiredParameterLabels = { 0, 1 })
	public static HeuristicsNet run(PluginContext context, XLog log, HeuristicsMinerSettings settings) {

		//XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
		FlexibleHeuristicsMiner fhm = new FlexibleHeuristicsMiner(context, log, settings);
		return fhm.mine();
	}
	
	@PluginVariant(variantLabel = "Mine Heuristics Net using Given Settings (2)", requiredParameterLabels = { 0, 1, 2 })
	public static HeuristicsNet run(PluginContext context, XLog log, HeuristicsMinerSettings settings, XLogInfo logInfo) {

		FlexibleHeuristicsMiner fhm = new FlexibleHeuristicsMiner(context, log, logInfo, settings);
		return fhm.mine();
	}*/
}