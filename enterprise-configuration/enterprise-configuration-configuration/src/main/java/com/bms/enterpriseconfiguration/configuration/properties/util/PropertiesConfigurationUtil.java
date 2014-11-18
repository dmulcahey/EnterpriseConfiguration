package com.bms.enterpriseconfiguration.configuration.properties.util;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;

public class PropertiesConfigurationUtil {
	
	public static PropertiesConfiguration buildPropertiesConfiguration(ClasspathResource classpathResource) throws ConfigurationException{
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
			    new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
			    .configure(new Parameters().fileBased().setURL(classpathResource.getURL()));
		return builder.getConfiguration();
	}

}
