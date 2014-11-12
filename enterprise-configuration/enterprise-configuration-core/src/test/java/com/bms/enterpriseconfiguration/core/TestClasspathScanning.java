package com.bms.enterpriseconfiguration.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Test;

import com.bms.enterpriseconfiguration.core.util.ResourceInfoUtil;
import com.google.common.collect.Sets;

public class TestClasspathScanning {
	
	@Test
	public void testComponentResources() throws IOException{
		Map<String, ResourceInfoCollection> resourceCollections = new AbstractResourceInfoCollectionResolver(){
		}.resolve(Sets.newHashSet(ResourceLocatorProviderFactory.create("ComponentResources", 2),
				ResourceLocatorProviderFactory.create("EnvironmentResources", 1),
				ResourceLocatorProviderFactory.create("SharedResources", 0),
				ResourceLocatorProviderFactory.create("SecureResources", 3)));
		
		assertNotNull(resourceCollections);
		assertEquals(4, resourceCollections.size());
		
		Logger.getAnonymousLogger().info(resourceCollections.toString());
		
		Logger.getAnonymousLogger().info(ResourceInfoUtil.getSimpleName(resourceCollections.get("ComponentResources").getResources().iterator().next()));
		
		Logger.getAnonymousLogger().info(ResourceInfoUtil.getSubdirectoryNamesFromResourceInfo(resourceCollections.get("ComponentResources").getResources(), "ComponentResources").toString());
		
		
		resourceCollections = new AbstractResourceInfoCollectionResolver(){
		}.resolve(Sets.newHashSet(ResourceLocatorProviderFactory.create("ComponentResources/ConfigurationCore/EnvironmentOverrides", 2)));
		
		Logger.getAnonymousLogger().info(resourceCollections.toString());
	}
	
}
