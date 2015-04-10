package com.bms.enterpriseconfiguration.configuration.component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import lombok.SneakyThrows;

import com.bms.enterpriseconfiguration.configuration.FileBasedConfiguration;
import com.bms.enterpriseconfiguration.configuration.classpath.CombinedClasspathConfigurationProxy;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.bms.enterpriseconfiguration.resources.classpath.util.ClasspathResourceUtil;
import com.google.common.base.Optional;
import com.google.common.reflect.Reflection;


public class ComponentConfigurationManager {
	public static final String ENVIRONMENT_ARGUMENT = "server.env";
	public static final String DEFAULT_ENVIRONMENT = System.getProperty(ENVIRONMENT_ARGUMENT);
	
	private static final Logger LOGGER = Logger.getLogger(ComponentConfigurationManager.class.getName());
	private static final ComponentConfigurationResolver COMPONENT_CONFIGURATION_RESOLVER = new ComponentConfigurationResolver();
	private static final Map<String, Map<String, ComponentConfiguration>> COMPONENT_CONFIGURATIONS_BY_ENVIRONMENT = new ConcurrentHashMap<String, Map<String, ComponentConfiguration>>();
	private static Optional<String> DEFAULT_COMPONENT_NAME;
	
	static{
		initialize();
	}

	private ComponentConfigurationManager(){
	}
	
	@SuppressWarnings("unchecked")
	public static FileBasedConfiguration<ClasspathResource> getConfiguration(final String componentName, final String configurationName, final String environment) {
		return Reflection.newProxy(FileBasedConfiguration.class, CombinedClasspathConfigurationProxy
				.builder()
				.componentConfigurationsByEnvironment(COMPONENT_CONFIGURATIONS_BY_ENVIRONMENT)
				.componentName(componentName)
				.configurationName(configurationName)
				.environment(environment)
				.build());
	}
	
	public static FileBasedConfiguration<ClasspathResource> getConfiguration(final String componentName, final String configurationName) {
		return getConfiguration(componentName, configurationName, DEFAULT_ENVIRONMENT);
	}
	
	public static ClasspathResource getResource(final String componentName, final String resourceName, final String environment){
		return COMPONENT_CONFIGURATIONS_BY_ENVIRONMENT.get(environment).get(componentName).getResource(resourceName);
	}
	
	public static FileBasedConfiguration<ClasspathResource> getConfiguration(final String configurationName) {
		return getComponentConfiguration().getConfiguration(configurationName);
	}
	
	public static ClasspathResource getResource(final String componentName, final String resourceName){
		return getResource(componentName, resourceName, DEFAULT_ENVIRONMENT);
	}
	
	public static ClasspathResource getResource(final String resourceName){
		return getComponentConfiguration().getResource(resourceName);
	}
	
	public static ComponentConfiguration getComponentConfiguration(String componentName, String environment){
		return COMPONENT_CONFIGURATIONS_BY_ENVIRONMENT.get(environment).get(componentName);
	}
	
	public static ComponentConfiguration getComponentConfiguration(String componentName){
		return getComponentConfiguration(componentName, DEFAULT_ENVIRONMENT);
	}
	
	public static ComponentConfiguration getComponentConfiguration(){
		if(DEFAULT_COMPONENT_NAME.isPresent()){
			return getComponentConfiguration(DEFAULT_COMPONENT_NAME.get(), DEFAULT_ENVIRONMENT);
		}
		throw new RuntimeException("A default component name was not set. Check your classpath resources to ensure that you have no more than 1 set of component resources.");
	}
	
	public static void reinitialize(){
		initialize();
	}
	
	@SneakyThrows
	private static void initialize(){
		LOGGER.info("initializing started...");
		Set<String> environmentNames = ClasspathResourceUtil.getSubdirectoryNamesFromParentDirectory(ComponentConfigurationResolver.ENVIRONMENT_RESOURCES);
		LOGGER.fine("Environments on classpath: " + environmentNames);
		Set<String> componentNames = ClasspathResourceUtil.getSubdirectoryNamesFromParentDirectory(ComponentConfigurationResolver.COMPONENT_RESOURCES);
		LOGGER.fine("Components on classpath: " + componentNames);
		if(componentNames.size() == 1){
			DEFAULT_COMPONENT_NAME = Optional.of(componentNames.iterator().next());
			LOGGER.fine("Default component name set to: " + DEFAULT_COMPONENT_NAME.get());
		}
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
		LOGGER.info("initializing complete!");
	}
	
}
