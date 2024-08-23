package org.processmining.plugins.pmenum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Export CSV", returnLabels = {}, returnTypes = {}, level = PluginLevel.Regular, parameterLabels = { "ModelTransitionTable", "File" }, userAccessible = true)
@UIExportPlugin(description = "Export Parameter Value List", extension = "csv")
public final class PMenumExportPlugin2  {
	
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "MITSUDA", email = "")
	@PluginVariant(requiredParameterLabels = { 0, 1 }, variantLabel = "Export Parameter Value CSV File")
	public void export(PluginContext context, ModelTransitionTable table, File file) throws IOException {
		
		FileOutputStream out = new FileOutputStream(file);
		String outString = table.getParameterValueListString();
		byte[] outBytes = outString.getBytes(StandardCharsets.UTF_8);
		
		for(int i = 0; i < outBytes.length; i++) {
			out.write(outBytes[i]);
		}
		
		out.flush();
		
		out.close();
	}
}
