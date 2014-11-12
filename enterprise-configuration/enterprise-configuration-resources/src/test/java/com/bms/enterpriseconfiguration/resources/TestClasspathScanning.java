package com.bms.enterpriseconfiguration.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Test;

import com.bms.enterpriseconfiguration.resources.ResourceInfoCollectionResolver.ResourceDefinition;
import com.bms.enterpriseconfiguration.resources.filter.ExtensionFilter;
import com.bms.enterpriseconfiguration.resources.filter.NotFilter;
import com.bms.enterpriseconfiguration.resources.util.ResourceInfoUtil;
import com.google.common.collect.Sets;

public class TestClasspathScanning {
	
	@Test
	public void testComponentResources() throws IOException{
		Map<String, ResourceInfoCollection> resourceCollections = new ResourceInfoCollectionMapResolver().resolve(
				Sets.newHashSet(
						ResourceLocatorProviderFactory.create("ComponentResources", 200),
						ResourceLocatorProviderFactory.create("EnvironmentResources", 100),
						ResourceLocatorProviderFactory.create("SharedResources", 0),
						ResourceLocatorProviderFactory.create("SecureResources", 300)
						));
		
		assertNotNull(resourceCollections);
		assertEquals(4, resourceCollections.size());
		
		Logger.getAnonymousLogger().info(resourceCollections.toString());
		
		Logger.getAnonymousLogger().info(ResourceInfoUtil.getSimpleName(resourceCollections.get("ComponentResources").getResources().iterator().next()));
		
		Logger.getAnonymousLogger().info(ResourceInfoUtil.getSubdirectoryNamesFromResourceInfo(resourceCollections.get("ComponentResources").getResources(), "ComponentResources").toString());
		
		
		ResourceInfoCollectionResolver resourceInfoCollectionResolver = new ResourceInfoCollectionResolver();
		ResourceInfoCollection componentResources = resourceCollections.get("ComponentResources");
		
		ResourceLocatorProvider resourceLocatorProvider = ResourceLocatorProviderFactory.create("ComponentResources", 200);
		resourceLocatorProvider.getFilters().add(new NotFilter(new ExtensionFilter(".properties")));
		
		ResourceInfoCollection componentResourcesPropertyFiles = resourceInfoCollectionResolver.doResolution(new ResourceDefinition(resourceLocatorProvider, null, componentResources));
		Logger.getAnonymousLogger().info(componentResourcesPropertyFiles.toString());
		
		ResourceInfoCollection junitEnvironmentOverrides = resourceCollections.get("ComponentResources").filter(ResourceLocatorProviderFactory.createFrom(resourceCollections.get("ComponentResources").getResourceLocatorProvider(), "ConfigurationCore/EnvironmentOverrides/JUNIT"));
		
		Logger.getAnonymousLogger().info(junitEnvironmentOverrides.toString());
		
		resourceCollections = new ResourceInfoCollectionMapResolver().resolve(
				Sets.newHashSet(
						ResourceLocatorProviderFactory.create("ComponentResources/ConfigurationCore/EnvironmentOverrides", 250)
						));
		
		Logger.getAnonymousLogger().info(resourceCollections.toString());
		
	}
	
}
