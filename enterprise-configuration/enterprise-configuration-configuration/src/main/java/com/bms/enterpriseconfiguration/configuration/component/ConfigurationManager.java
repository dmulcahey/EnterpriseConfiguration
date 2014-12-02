package com.bms.enterpriseconfiguration.configuration.component;

import java.lang.reflect.Method;

import com.bms.enterpriseconfiguration.configuration.classpath.ClasspathConfiguration;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;

public class ConfigurationManager {

	public ClasspathConfiguration getConfiguration(final String componentName, final String configurationName, final String environment) {
		return Reflection.newProxy(ClasspathConfiguration.class, new AbstractInvocationHandler(){

			@Override
			protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
				ComponentConfigurationResolver componentConfigurationResolver = new ComponentConfigurationResolver();
				ComponentConfigurationResolver.Criteria criteria = new ComponentConfigurationResolver.Criteria();
				criteria.setComponentName(componentName);
				criteria.setEnvironment(environment);
				ComponentConfiguration componentConfiguration = componentConfigurationResolver.doResolution(criteria);
				return method.invoke(componentConfiguration.getConfiguration(configurationName), args);
			}

			@Override
			public String toString() {
				ComponentConfigurationResolver componentConfigurationResolver = new ComponentConfigurationResolver();
				ComponentConfigurationResolver.Criteria criteria = new ComponentConfigurationResolver.Criteria();
				criteria.setComponentName(componentName);
				criteria.setEnvironment(environment);
				ComponentConfiguration componentConfiguration = componentConfigurationResolver.doResolution(criteria);
				return componentConfiguration.getConfiguration(configurationName).toString();
			}
			
		});
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
