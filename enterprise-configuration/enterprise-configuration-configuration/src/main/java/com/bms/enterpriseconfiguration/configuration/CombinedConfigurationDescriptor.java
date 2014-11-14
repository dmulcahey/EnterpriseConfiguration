package com.bms.enterpriseconfiguration.configuration;

import java.util.Set;

import com.bms.enterpriseconfiguration.resources.ResourceProvider;
import com.google.common.collect.Sets;

public class CombinedConfigurationDescriptor {
	
	private String name;
	private Set<ResourceProvider<?>> resourceProviders;
	
	public CombinedConfigurationDescriptor(String name) {
		super();
		this.name = name;
	}
	
	public CombinedConfigurationDescriptor() {
		super();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<ResourceProvider<?>> getResourceProviders() {
		if(resourceProviders == null){
			resourceProviders = Sets.newHashSet();
		}
		return resourceProviders;
	}

	@Override
	public String toString() {
		return "CombinedConfigurationDescriptor [name=" + name
				+ ", resourceProviders=" + resourceProviders + "]";
	}
	
}
