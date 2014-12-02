package com.bms.enterpriseconfiguration.configuration.component;

import java.util.Map;

import com.bms.enterpriseconfiguration.configuration.classpath.CombinedClasspathConfiguration;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.collect.Maps;

//TODO make this immutable and create a builder? 
public class ComponentConfiguration {

	private String name;
	private Map<String, CombinedClasspathConfiguration> configurations = Maps.newHashMap();
	private Map<String, ClasspathResource> resources = Maps.newHashMap();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Map<String, CombinedClasspathConfiguration> getConfigurations() {
		return configurations;
	}

	public Map<String, ClasspathResource> getResources() {
		return resources;
	}

	@Override
	public String toString() {
		return "ComponentConfiguration [name=" + name + ", configurations="
				+ configurations + ", resources=" + resources + "]";
	}

}
