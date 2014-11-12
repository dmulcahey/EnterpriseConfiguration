package com.bms.enterpriseconfiguration.resources;

import java.util.Set;

import com.bms.enterpriseconfiguration.resources.ResourceInfoCollectionResolver.ResourceDefinition;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath.ResourceInfo;

public class ResourceInfoCollection extends AbstractPrintableResource {

	private ResourceLocatorProvider resourceLocatorProvider;
	private Set<ResourceInfo> resources;

	public ResourceLocatorProvider getResourceLocatorProvider() {
		return resourceLocatorProvider;
	}

	public void setResourceLocatorProvider(ResourceLocatorProvider resourceLocatorProvider) {
		this.resourceLocatorProvider = resourceLocatorProvider;
	}

	public Set<ResourceInfo> getResources() {
		if(resources == null){
			resources = Sets.newHashSet();
		}
		return resources;
	}
	
	public ResourceInfoCollection filter(ResourceLocatorProvider aResourceLocatorProvider){
		return new ResourceInfoCollectionResolver().doResolution(new ResourceDefinition(ResourceLocatorProviderFactory.createCombined(aResourceLocatorProvider.getResourceLocator(), resourceLocatorProvider), null, this));
	}
	
}
