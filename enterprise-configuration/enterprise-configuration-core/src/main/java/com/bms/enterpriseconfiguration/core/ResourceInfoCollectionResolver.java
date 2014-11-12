package com.bms.enterpriseconfiguration.core;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;

public class ResourceInfoCollectionResolver extends AbstractResolver<Set<ResourceLocatorProvider>, Map<String, ResourceInfoCollection>> {
	
	protected final Map<String, ResourceInfoCollection> doResolution(Set<ResourceLocatorProvider> resourceLocatorProviders){
		Set<ResourceInfo> resourceInformation;
		try {
			resourceInformation = ClassPath.from(Thread.currentThread().getContextClassLoader()).getResources();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Map<String, ResourceInfoCollection> resourceInfoCollections = Maps.newHashMapWithExpectedSize(resourceLocatorProviders.size());
		
		for(final ResourceLocatorProvider resourceLocatorProvider : resourceLocatorProviders){
			ResourceInfoCollection resourceInfoCollection = new ResourceInfoCollection();
			resourceInfoCollection.setResourceLocatorProvider(resourceLocatorProvider);
			resourceInfoCollection.getResources().addAll(Sets.filter(resourceInformation, new Predicate<ResourceInfo>(){
				@Override
				public boolean apply(ResourceInfo input) {
					return input.getResourceName().contains(resourceLocatorProvider.getResourceLocator());
				}
			}));
			resourceInfoCollections.put(resourceLocatorProvider.getResourceLocator(), resourceInfoCollection);
		}
		
		return resourceInfoCollections;
	}
	
}
