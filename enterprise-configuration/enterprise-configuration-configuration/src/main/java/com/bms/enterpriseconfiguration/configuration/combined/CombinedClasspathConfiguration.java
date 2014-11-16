package com.bms.enterpriseconfiguration.configuration.combined;

import com.bms.enterpriseconfiguration.configuration.Configuration;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptor;
import com.bms.enterpriseconfiguration.resources.AbstractResource;

public class CombinedClasspathConfiguration<R extends AbstractResource> extends org.apache.commons.configuration2.CombinedConfiguration implements Configuration<R> {

	private ConfigurationDescriptor<R> combinedConfigurationDescriptor;

	@Override
	public ConfigurationDescriptor<R> getCombinedConfigurationDescriptor() {
		return combinedConfigurationDescriptor;
	}

	public void setCombinedConfigurationDescriptor(ConfigurationDescriptor<R> combinedConfigurationDescriptor) {
		this.combinedConfigurationDescriptor = combinedConfigurationDescriptor;
	}
	
}
