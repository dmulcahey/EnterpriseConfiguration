package com.bms.enterpriseconfiguration.configuration.component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import lombok.SneakyThrows;

import com.bms.enterpriseconfiguration.configuration.classpath.ClasspathConfiguration;
import com.bms.enterpriseconfiguration.configuration.classpath.CombinedClasspathConfigurationProxy;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.bms.enterpriseconfiguration.resources.classpath.util.ClasspathResourceUtil;
import com.google.common.reflect.Reflection;


public class ComponentConfigurationManager {
	public static final String ENVIRONMENT_ARGUMENT = "server.env";
	public static final String DEFAULT_ENVIRONMENT = System.getProperty(ENVIRONMENT_ARGUMENT);
	
	private static final Logger LOGGER = Logger.getLogger(ComponentConfigurationManager.class.getName());
	private static final ComponentConfigurationResolver COMPONENT_CONFIGURATION_RESOLVER = new ComponentConfigurationResolver();
	private static final Map<String, Map<String, ComponentConfiguration>> COMPONENT_CONFIGURATIONS_BY_ENVIRONMENT = new ConcurrentHashMap<String, Map<String, ComponentConfiguration>>();
	
	static{
		initialize();
	}

	private ComponentConfigurationManager(){
	}
	
	public static ClasspathConfiguration getConfiguration(final String componentName, final String configurationName, final String environment) {
		return Reflection.newProxy(ClasspathConfiguration.class, CombinedClasspathConfigurationProxy
				.builder()
				.componentConfigurationsByEnvironment(COMPONENT_CONFIGURATIONS_BY_ENVIRONMENT)
				.componentName(componentName)
				.configurationName(configurationName)
				.environment(environment)
				.build());
	}
	
	public static ClasspathConfiguration getConfiguration(final String componentName, final String configurationName) {
		return getConfiguration(componentName, configurationName, DEFAULT_ENVIRONMENT);
	}
	
	public static ClasspathResource getResource(final String componentName, final String resourceName, final String environment){
		return COMPONENT_CONFIGURATIONS_BY_ENVIRONMENT.get(environment).get(componentName).getResource(resourceName);
	}
	
	public static ClasspathResource getResource(final String componentName, final String resourceName){
		return getResource(componentName, resourceName, DEFAULT_ENVIRONMENT);
	}
	
	public static void reinitialize(){
		initialize();
	}
	
	@SneakyThrows
	private static void initialize(){
		LOGGER.info("initializing ComponentConfigurationManager started...");
		Set<String> environmentNames = ClasspathResourceUtil.getSubdirectoryNamesFromParentDirectory(ComponentConfigurationResolver.ENVIRONMENT_RESOURCES);
		LOGGER.fine("Environments on classpath: " + environmentNames);
		Set<String> componentNames = ClasspathResourceUtil.getSubdirectoryNamesFromParentDirectory(ComponentConfigurationResolver.COMPONENT_RESOURCES);
		LOGGER.fine("Components on classpath: " + componentNames);
		for(String environmentName : environmentNames){
			LOGGER.config("Initializing component configurations for environment: " + environmentName);
			ConcurrentHashMap<String, ComponentConfiguration> componentConfigurations = new ConcurrentHashMap<String, ComponentConfiguration>();
			for(String componentName : componentNames){
				LOGGER.config("Initializing component configuration: " + componentName + " for environment: " + environmentName);
				componentConfigurations.put(componentName, COMPONENT_CONFIGURATION_RESOLVER.resolve(new ComponentConfigurationResolver.Criteria(componentName, environmentName)));
			}
			COMPONENT_CONFIGURATIONS_BY_ENVIRONMENT.put(environmentName, componentConfigurations);
		}
		LOGGER.fine("All fully resolved component configurations by environment: " + COMPONENT_CONFIGURATIONS_BY_ENVIRONMENT);
		LOGGER.info("initializing ComponentConfigurationManager complete!");
	}
	
}
