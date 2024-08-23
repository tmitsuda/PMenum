package org.processmining.plugins.pmenum;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Visualize HeuristicsNet with Annotations", level = PluginLevel.Regular, parameterLabels = { "ModelTransitionTable" }, returnLabels = { "HN Annotated Visualization - No Semantics" }, returnTypes = { JComponent.class })
@Visualizer
public class PMenumVisualizerPlugin {

	@PluginVariant(requiredParameterLabels = { 0 })
	public static JComponent visualize(PluginContext context, ModelTransitionTable table) {
		
		return PMenumVisualizer.visualizeGraph(context, table);
		
		/*XLog log = table.getLog();
		
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
		
		HeuristicsMinerSettings minerSettings = new HeuristicsMinerSettings();
		minerSettings.setClassifier(defaultClassifier);
		
		HeuristicsNet net = FlexibleHeuristicsMinerPlugin.run(context, log, minerSettings, loginfo);
			
		AnnotatedVisualizationGenerator generator = new AnnotatedVisualizationGenerator();
		
		AnnotatedVisualizationSettings settings = new AnnotatedVisualizationSettings();
		HeuristicsNetGraph graph = generator.generate(net, settings);
		
		return OriginalVisualizer.visualizeGraph(graph, net, settings, context.getProgress());*/
	}
}