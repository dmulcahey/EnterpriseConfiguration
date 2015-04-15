package com.bms.enterpriseconfiguration.configuration.component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.bms.enterpriseconfiguration.configuration.classpath.CombinedClasspathConfiguration;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.collect.Maps;

public class ComponentConfiguration {

	private final String name;
	private final Map<String, CombinedClasspathConfiguration> configurations;
	private final Map<String, ClasspathResource> resources;
	
	public String getName() {
		return name;
	}

	public Map<String, CombinedClasspathConfiguration> getConfigurations() {
		return configurations;
	}

	public Map<String, ClasspathResource> getResources() {
		return resources;
	}
	
	public CombinedClasspathConfiguration getConfiguration(String configurationName) {
		return getConfigurations().get(configurationName);
	}
	
	public ClasspathResource getResource(String resourceLocator){
		return getResources().get(resourceLocator);
	}

	@Override
	public String toString() {
		return "ComponentConfiguration [name=" + name + ", configurations="
				+ configurations + ", resources=" + resources + "]";
	}
	
	public static Builder builder(){
		return new Builder();
	}
	
	public static class Builder {
		private String name;
		private Map<String, CombinedClasspathConfiguration> configurations = Maps.newHashMap();
		private Map<String, ClasspathResource> resources = Maps.newHashMap();
		
		public Builder name(String name){
			this.name = name;
			return this;
		}
		
		public Builder withConfiguration(CombinedClasspathConfiguration configuration){
			this.configurations.put(configuration.getConfigurationDescriptor().getName(), configuration);
			return this;
		}
		
		public Builder withConfigurations(Collection<CombinedClasspathConfiguration> configurations){
			for(CombinedClasspathConfiguration configuration : configurations){
				this.withConfiguration(configuration);
			}
			return this;
		}
		
		public Builder withResource(ClasspathResource resource){
			this.resources.put(resource.getName(), resource);
			return this;
		}
		
		public Builder withResources(Collection<ClasspathResource> resources){
			for(ClasspathResource resource: resources){
				this.withResource(resource);
			}
			return this;
		}
		
		public Builder withResources(Map<String, ClasspathResource> resources){
			this.resources.putAll(resources);
			return this;
		}
		
		public ComponentConfiguration build(){
			return new ComponentConfiguration(this);
		}
	}
	
	private ComponentConfiguration(Builder builder){
		this.configurations = Collections.unmodifiableMap(builder.configurations);
		this.name = builder.name;
		this.resources = Collections.unmodifiableMap(builder.resources);
	}

}
