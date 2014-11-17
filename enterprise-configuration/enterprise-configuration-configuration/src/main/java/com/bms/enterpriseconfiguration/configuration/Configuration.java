package com.bms.enterpriseconfiguration.configuration;

import java.util.Properties;

import com.bms.enterpriseconfiguration.resources.AbstractResource;

public interface Configuration<R extends AbstractResource> extends org.apache.commons.configuration2.Configuration {

	ConfigurationDescriptor<R> getCombinedConfigurationDescriptor();
	
	/**
	 * Returns the information contained in this Configuration as a Properties object.
	 * @return Properties 
	 */
	Properties getAsProperties();
	
	/**
	 * Returns the information contained in this Configuration as a Properties object that can be displayed without exposing secure information.
	 * @return Properties 
	 */
	Properties getAsPrintableProperties();
	
}
