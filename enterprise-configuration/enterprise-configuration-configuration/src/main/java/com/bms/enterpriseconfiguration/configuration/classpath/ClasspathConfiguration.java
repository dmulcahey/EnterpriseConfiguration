package com.bms.enterpriseconfiguration.configuration.classpath;

import java.util.Properties;

import com.bms.enterpriseconfiguration.configuration.Configuration;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;

public interface ClasspathConfiguration extends Configuration<ClasspathResource> {

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