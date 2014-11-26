package com.bms.enterpriseconfiguration.configuration.component;

import static org.junit.Assert.assertNotNull;

import java.util.logging.Logger;

import org.junit.Test;

public class ComponentConfigurationResolverTest {

	@Test
	public void testComponentConfigurationResolver(){
		ComponentConfigurationResolver componentConfigurationResolver = new ComponentConfigurationResolver();
		ComponentConfigurationResolver.Criteria criteria = new ComponentConfigurationResolver.Criteria();
		criteria.setComponentName("Configuration");
		criteria.setEnvironment("JUNIT");
		ComponentConfiguration componentConfiguration = componentConfigurationResolver.resolve(criteria);
		assertNotNull(componentConfiguration);
		Logger.getAnonymousLogger().info(componentConfiguration.toString());
	}
	
}
