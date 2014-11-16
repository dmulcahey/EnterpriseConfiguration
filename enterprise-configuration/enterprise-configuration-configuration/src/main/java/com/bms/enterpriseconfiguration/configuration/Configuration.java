package com.bms.enterpriseconfiguration.configuration;

import com.bms.enterpriseconfiguration.resources.AbstractResource;

public interface Configuration<R extends AbstractResource> extends org.apache.commons.configuration2.Configuration{

	ConfigurationDescriptor<R> getCombinedConfigurationDescriptor();
	
}
