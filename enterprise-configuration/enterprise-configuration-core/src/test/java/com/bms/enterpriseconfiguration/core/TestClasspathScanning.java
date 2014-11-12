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
		}.resolve(Sets.newHashSet(new AbstractResourceLocatorProvider() {
					@Override
					public String getResourceLocator() {
						return "ComponentResources";
					}

					@Override
					public int getOrder() {
						return 2;
					}
				},new AbstractResourceLocatorProvider() {
					@Override
					public String getResourceLocator() {
						return "EnvironmentResources";
					}

					@Override
					public int getOrder() {
						return 1;
					}
				},new AbstractResourceLocatorProvider() {
					@Override
					public String getResourceLocator() {
						return "SharedResources";
					}

					@Override
					public int getOrder() {
						return 0;
					}
				},new AbstractResourceLocatorProvider() {
					@Override
					public String getResourceLocator() {
						return "SecureResources";
					}

					@Override
					public int getOrder() {
						return 3;
					}
				}));
		
		assertNotNull(resourceCollections);
		assertEquals(4, resourceCollections.size());
		
		Logger.getAnonymousLogger().info(resourceCollections.toString());
		
		Logger.getAnonymousLogger().info(ResourceInfoUtil.getSimpleName(resourceCollections.get("ComponentResources").getResources().iterator().next()));
	}
	
}
