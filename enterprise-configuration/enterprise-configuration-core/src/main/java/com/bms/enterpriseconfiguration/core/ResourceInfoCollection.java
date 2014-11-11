package com.bms.enterpriseconfiguration.core;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath.ResourceInfo;

public class ResourceInfoCollection {

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

	@Override
	public String toString() {
		return "ResourceInfoCollection [resourceLocatorProvider="
				+ resourceLocatorProvider + ", resources=" + resources + "]";
	}
	
}
