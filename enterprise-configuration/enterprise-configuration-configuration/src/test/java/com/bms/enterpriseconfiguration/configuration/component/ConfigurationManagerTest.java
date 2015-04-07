package com.bms.enterpriseconfiguration.configuration.component;

import java.util.logging.Logger;

import org.jasypt.util.text.StrongTextEncryptor;
import org.junit.Test;

import com.bms.enterpriseconfiguration.configuration.classpath.ClasspathConfiguration;

public class ConfigurationManagerTest {

	@Test
	public void testGetConfiguration(){
		ConfigurationManager configurationManager = new ConfigurationManager();
		ClasspathConfiguration configuration = configurationManager.getConfiguration("Configuration", "junk", "JUNIT");
		Logger.getAnonymousLogger().info(configuration.toString());
		
		ClasspathConfiguration secureConfiguration = configurationManager.getConfiguration("Configuration", "FileShare", "JUNIT");
		
		Logger.getAnonymousLogger().info(secureConfiguration.toString());
		
		
		Logger.getAnonymousLogger().info(secureConfiguration.getString("encrypted"));
		
		Logger.getAnonymousLogger().info("" + secureConfiguration.getProperty("encrypted"));
		
		Logger.getAnonymousLogger().info("" + secureConfiguration.getAsProperties().getProperty("encrypted"));
		
		Logger.getAnonymousLogger().info(secureConfiguration.getString("encrypted"));
	}
	
	@Test
	public void encryptValue(){
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPassword("TESTING");
		Logger.getAnonymousLogger().info(textEncryptor.encrypt("encryptMe"));
	}
	
}
