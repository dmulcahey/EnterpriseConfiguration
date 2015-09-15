package com.bms.enterpriseconfiguration.configuration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.logging.Logger;

import lombok.SneakyThrows;

import org.junit.Test;

import com.bms.enterpriseconfiguration.resources.classpath.ClassPath;

public class ComponentConfigurationResolverTest {

	@Test
	@SneakyThrows
	public void testComponentConfigurationResolver(){
		ComponentConfigurationResolver componentConfigurationResolver = new ComponentConfigurationResolver();
		ComponentConfigurationResolver.Criteria criteria = new ComponentConfigurationResolver.Criteria("Configuration", "JUNIT", ClassPath.from(Thread.currentThread().getContextClassLoader()));
		ComponentConfiguration componentConfiguration = componentConfigurationResolver.resolve(criteria);
		assertNotNull(componentConfiguration);
		Logger.getAnonymousLogger().info(componentConfiguration.toString());
		assertEquals(5, componentConfiguration.getConfigurations().size());
		assertEquals(6, componentConfiguration.getResources().size());
		assertNotNull(componentConfiguration.getResources().get("junkXML.xml"));
		assertNotNull(componentConfiguration.getResources().get("someFolder/someResource.xml"));
		assertNotNull(componentConfiguration.getResources().get("someOtherFolder/someResource.xml"));
	}
	
}
