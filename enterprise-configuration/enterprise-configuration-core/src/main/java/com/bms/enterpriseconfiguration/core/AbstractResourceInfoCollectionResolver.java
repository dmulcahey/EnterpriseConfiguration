package com.bms.enterpriseconfiguration.core;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;

public abstract class AbstractResourceInfoCollectionResolver extends AbstractResolver<ClassPath, Map<String, ResourceInfoCollection>> {

	protected abstract Set<ResourceLocatorProvider> getResourceLocatorProviders();
	
	protected final Map<String, ResourceInfoCollection> doResolution(ClassPath classPath){
		Set<ResourceInfo> resourceInformation = classPath.getResources();
		Map<String, ResourceInfoCollection> resourceInfoCollections = Maps.newHashMapWithExpectedSize(getResourceLocatorProviders().size());
		
		for(ResourceLocatorProvider resourceLocatorProvider : getResourceLocatorProviders()){
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
