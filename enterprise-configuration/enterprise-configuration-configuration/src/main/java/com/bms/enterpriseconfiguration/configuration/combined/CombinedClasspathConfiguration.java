package com.bms.enterpriseconfiguration.configuration.combined;

import java.util.Properties;

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

	public CombinedClasspathConfiguration(NodeCombiner comb) {
		super(comb);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getAsPrintableProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
