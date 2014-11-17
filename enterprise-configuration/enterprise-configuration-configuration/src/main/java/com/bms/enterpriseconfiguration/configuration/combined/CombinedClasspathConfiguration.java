package com.bms.enterpriseconfiguration.configuration.combined;

import java.util.Properties;

import org.apache.commons.configuration2.ConfigurationConverter;
import org.apache.commons.configuration2.tree.NodeCombiner;
import org.apache.commons.configuration2.tree.OverrideCombiner;

import com.bms.enterpriseconfiguration.configuration.Configuration;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptor;
import com.bms.enterpriseconfiguration.resources.AbstractResource;

public class CombinedClasspathConfiguration<R extends AbstractResource> extends org.apache.commons.configuration2.CombinedConfiguration implements Configuration<R> {
	
	private ConfigurationDescriptor<R> combinedConfigurationDescriptor;
	
	public CombinedClasspathConfiguration() {
		this(new OverrideCombiner());
	}

	public CombinedClasspathConfiguration(NodeCombiner nodeCombiner) {
		super(nodeCombiner);
		this.getInterpolator().setEnableSubstitutionInVariables(true);
	}

	@Override
	public ConfigurationDescriptor<R> getCombinedConfigurationDescriptor() {
		return combinedConfigurationDescriptor;
	}

	public void setCombinedConfigurationDescriptor(ConfigurationDescriptor<R> combinedConfigurationDescriptor) {
		this.combinedConfigurationDescriptor = combinedConfigurationDescriptor;
	}

	@Override
	public Properties getAsProperties() {
		return ConfigurationConverter.getProperties(this);
	}

	@Override
	public Properties getAsPrintableProperties() {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(combinedConfigurationDescriptor.toString());
		sb.append("\n\nProperties:\n");
		sb.append(getAsProperties().toString());
		sb.append("\n\n");
		return sb.toString();
	}
	
}
