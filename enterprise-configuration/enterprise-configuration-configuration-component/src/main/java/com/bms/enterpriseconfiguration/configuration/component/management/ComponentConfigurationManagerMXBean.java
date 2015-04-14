package com.bms.enterpriseconfiguration.configuration.component.management;

public interface ComponentConfigurationManagerMXBean {

	void reinitialize();
	
	/**
	 * @param componentName
	 */
	void logComponentConfiguration(String componentName);
	
	/**
	 * @param componentName
	 * @param environmentName
	 */
	void logComponentConfiguration(String componentName, String environmentName);
	
	/**
	 * @param componentName
	 * @param configurationName
	 */
	void logConfiguration(String componentName, String configurationName);
	
	/**
	 * @param componentName
	 * @param configurationName
	 * @param environmentName
	 */
	void logConfiguration(String componentName, String configurationName, String environmentName);
	
	/**
	 * @param componentName
	 * @param configurationName
	 * @param key
	 */
	void logConfigurationEntry(String componentName, String configurationName, String key);
	
	/**
	 * @param componentName
	 * @param configurationName
	 * @param key
	 * @param environmentName
	 */
	void logConfigurationEntry(String componentName, String configurationName, String key, String environmentName);
	
	/**
	 * @param componentName
	 * @param configurationFileName
	 */
	void logComponentConfigurationFile(String componentName, String configurationFileName);
	
	/**
	 * @param componentName
	 * @param configurationFileName
	 * @param environmentName
	 */
	void logComponentConfigurationFile(String componentName, String configurationFileName, String environmentName);

	/**
	 * @param componentName
	 * @param configurationFileName
	 */
	void logSharedConfigurationFile(String componentName, String configurationFileName);
	
	/**
	 * @param componentName
	 * @param configurationFileName
	 * @param environmentName
	 */
	void logSharedConfigurationFile(String componentName, String configurationFileName, String environmentName);

	/**
	 * @param componentName
	 * @param configurationFileName
	 */
	void logSecureConfigurationFile(String componentName, String configurationFileName);
	
	/**
	 * @param componentName
	 * @param configurationFileName
	 * @param environmentName
	 */
	void logSecureConfigurationFile(String componentName, String configurationFileName, String environmentName);

	/**
	 * @param componentName
	 * @param configurationFileName
	 */
	void logEnvironmentConfigurationFile(String componentName, String configurationFileName);
	
	/**
	 * @param componentName
	 * @param configurationFileName
	 * @param environmentName
	 */
	void logEnvironmentConfigurationFile(String componentName, String configurationFileName, String environmentName);
	
	void logAllManifestInformation();
	
}
