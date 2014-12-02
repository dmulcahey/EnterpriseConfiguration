package com.bms.enterpriseconfiguration.configuration.component;

import java.util.logging.Logger;

import org.junit.Test;

import com.bms.enterpriseconfiguration.configuration.classpath.ClasspathConfiguration;

public class ConfigurationManagerTest {

	@Test
	public void testGetConfiguration(){
		ConfigurationManager configurationManager = new ConfigurationManager();
		ClasspathConfiguration configuration = configurationManager.getConfiguration("Configuration", "junk", "JUNIT");
		Logger.getAnonymousLogger().info(configuration.toString());
	}
	
}
