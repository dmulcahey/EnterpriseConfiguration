package com.bms.enterpriseconfiguration.configuration.classpath.util;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;

public class CommonsConfigurationUtil {
	
	public static final String PROPERTIES_EXTENSION = ".properties";
	public static final String XML_EXTENSION = ".xml";
	
	public static Configuration buildConfiguration(ClasspathResource classpathResource) throws ConfigurationException {
		if(classpathResource.getResourceName().endsWith(PROPERTIES_EXTENSION)){
			FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
				    new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
				    .configure(new Parameters().fileBased().setURL(classpathResource.getURL()));
			return builder.getConfiguration();
		}else if(classpathResource.getResourceName().endsWith(XML_EXTENSION)){
			FileBasedConfigurationBuilder<XMLConfiguration> builder =
				    new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
				    .configure(new Parameters().fileBased().setURL(classpathResource.getURL()));
			return builder.getConfiguration();
		}
		throw new RuntimeException("Unable to build a configuration object for: " + classpathResource.getResourceName());
	}

}
