package com.bms.enterpriseconfiguration.configuration.component.management;

import java.util.logging.Logger;

import com.bms.enterpriseconfiguration.configuration.component.ComponentConfigurationManager;

public class ComponentConfigurationManagerMXBeanImpl implements ComponentConfigurationManagerMXBean {

	private static final Logger LOGGER = Logger.getLogger(ComponentConfigurationManagerMXBeanImpl.class.getName());
	
	@Override
	public void reinitialize() {
		LOGGER.info("reinitializing ComponentConfigurationManager via JMX request...");
		ComponentConfigurationManager.reinitialize();
	}

	@Override
	public void logComponentConfiguration(String componentName) {
		LOGGER.info(ComponentConfigurationManager.getComponentConfiguration().toString());
	}

	@Override
	public void logComponentConfiguration(String componentName, String environmentName) {
		LOGGER.info(ComponentConfigurationManager.getComponentConfiguration(componentName, environmentName).toString());
	}

	@Override
	public void logConfiguration(String componentName, String configurationName) {
		LOGGER.info(ComponentConfigurationManager.getConfiguration(componentName, configurationName).toString());
	}

	@Override
	public void logConfiguration(String componentName, String configurationName, String environmentName) {
		LOGGER.info(ComponentConfigurationManager.getConfiguration(componentName, configurationName, environmentName).toString());
	}

	@Override
	public void logConfigurationEntry(String componentName, String configurationName, String key) {
		LOGGER.info(ComponentConfigurationManager.getConfiguration(componentName, configurationName).getString(key));

	}

	@Override
	public void logConfigurationEntry(String componentName, String configurationName, String key, String environmentName) {
		LOGGER.info(ComponentConfigurationManager.getConfiguration(componentName, configurationName, environmentName).getString(key));
	}

	@Override
	public void logComponentConfigurationFile(String componentName, String configurationFileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logComponentConfigurationFile(String componentName, String configurationFileName, String environmentName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logSharedConfigurationFile(String componentName, String configurationFileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logSharedConfigurationFile(String componentName, String configurationFileName, String environmentName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logSecureConfigurationFile(String componentName, String configurationFileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logSecureConfigurationFile(String componentName, String configurationFileName, String environmentName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logEnvironmentConfigurationFile(String componentName, String configurationFileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logEnvironmentConfigurationFile(String componentName, String configurationFileName, String environmentName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logAllManifestInformation() {
		// TODO Auto-generated method stub

	}

}
