package com.bms.enterpriseconfiguration.core;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;

public class TestClasspathScanning {
	
	@Test
	public void testComponentResources() throws IOException{
		Map<String, ResourceInfoCollection> resourceCollections = new AbstractResourceInfoCollectionResolver(){
			@Override
			protected Set<ResourceLocatorProvider> getResourceLocatorProviders() {
				Set<ResourceLocatorProvider> resourceLocatorProviders = Sets.newHashSet();
				resourceLocatorProviders.add(new AbstractResourceLocatorProvider() {
					@Override
					public String getResourceLocator() {
						return "ComponentResources";
					}
				});
				resourceLocatorProviders.add(new AbstractResourceLocatorProvider() {
					@Override
					public String getResourceLocator() {
						return "EnvironmentResources";
					}
				});
				resourceLocatorProviders.add(new AbstractResourceLocatorProvider() {
					@Override
					public String getResourceLocator() {
						return "SharedResources";
					}
				});
				resourceLocatorProviders.add(new AbstractResourceLocatorProvider() {
					@Override
					public String getResourceLocator() {
						return "SecureResources";
					}
				});
				return resourceLocatorProviders;
			}
		}.resolve(ClassPath.from(Thread.currentThread().getContextClassLoader()));
		
		assertNotNull(resourceCollections);
		assertEquals(4, resourceCollections.size());
		
		Logger.getAnonymousLogger().info(resourceCollections.toString());
		
	}
	
}
