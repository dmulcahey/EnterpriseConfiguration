package com.bms.enterpriseconfiguration.configuration;

import java.util.Set;

import com.bms.enterpriseconfiguration.resources.AbstractResource;
import com.google.common.collect.Sets;

public class ConfigurationDescriptor<T extends AbstractResource> {
	
	private String name;
	private Set<T> resources;
	
	public ConfigurationDescriptor(String name) {
		super();
		this.name = name;
	}
	
	public ConfigurationDescriptor() {
		super();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Set<T> getResources() {
		if(resources == null){
			resources = Sets.newHashSet();
		}
		return resources;
	}

	@Override
	public String toString() {
		return "CombinedConfigurationDescriptor [name=" + name + ", resources="
				+ resources + "]";
	}
	
}
