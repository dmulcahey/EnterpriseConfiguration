package com.bms.enterpriseconfiguration.resources;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.bms.enterpriseconfiguration.core.AbstractResolver;
import com.bms.enterpriseconfiguration.resources.ResourceInfoCollectionResolver.ResourceDefinition;
import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;

public class ResourceInfoCollectionMapResolver extends AbstractResolver<Set<ResourceLocatorProvider>, Map<String, ResourceInfoCollection>> {
	
	protected final Map<String, ResourceInfoCollection> doResolution(Set<ResourceLocatorProvider> resourceLocatorProviders){
		ClassPath classPath = null;
		try {
			classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Map<String, ResourceInfoCollection> resourceInfoCollections = Maps.newHashMapWithExpectedSize(resourceLocatorProviders.size());
		
		for(final ResourceLocatorProvider resourceLocatorProvider : resourceLocatorProviders){
			resourceInfoCollections.put(resourceLocatorProvider.getName(), new ResourceInfoCollectionResolver().doResolution(new ResourceDefinition(resourceLocatorProvider, classPath, null)));
		}
		
		return resourceInfoCollections;
	}
	
}
