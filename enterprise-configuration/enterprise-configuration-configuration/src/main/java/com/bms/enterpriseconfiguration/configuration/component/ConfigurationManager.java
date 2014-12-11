package com.bms.enterpriseconfiguration.configuration.component;

import com.bms.enterpriseconfiguration.configuration.classpath.ClasspathConfiguration;
import com.bms.enterpriseconfiguration.configuration.classpath.CombinedClasspathConfigurationProxy;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.reflect.Reflection;

public class ConfigurationManager {

	public ClasspathConfiguration getConfiguration(final String componentName, final String configurationName, final String environment) {
		return Reflection.newProxy(ClasspathConfiguration.class, CombinedClasspathConfigurationProxy
				.builder()
				.componentConfigurationResolver(new ComponentConfigurationResolver())
				.componentName(componentName)
				.configurationName(configurationName)
				.environment(environment)
				.build());
	}
	
	public ClasspathConfiguration getConfiguration(final String componentName, final String configurationName) {
		return getConfiguration(componentName, configurationName, System.getProperty("server.env"));
	}
	
	public ClasspathResource getResource(final String componentName, final String environment, final String resourceName){
		return null;
	}
	
	public ClasspathResource getResource(final String componentName, final String resourceName){
		return null;
	}
	
}
