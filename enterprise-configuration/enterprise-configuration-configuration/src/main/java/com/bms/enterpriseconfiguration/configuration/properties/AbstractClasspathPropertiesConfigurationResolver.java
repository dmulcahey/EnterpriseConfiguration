package com.bms.enterpriseconfiguration.configuration.properties;

import java.util.Set;

import com.bms.enterpriseconfiguration.configuration.AbstractConfigurationResolver;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptor;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptorResolver;
import com.bms.enterpriseconfiguration.resources.ResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.collect.Sets;

public abstract class AbstractClasspathPropertiesConfigurationResolver<T extends ClasspathPropertiesConfiguration> extends AbstractConfigurationResolver<ClasspathResource, T>{

	public AbstractClasspathPropertiesConfigurationResolver(ConfigurationDescriptorResolver<ClasspathResource> configurationDescriptorResolver) {
		super(configurationDescriptorResolver);
	}
	
	protected abstract T resolveClasspathPropertiesConfiguration(ConfigurationDescriptor<ClasspathResource> configurationDescriptor) throws Exception;
	
	@Override
	protected Set<T> doResolution(Set<? extends ResourceProvider<Set<ClasspathResource>>> input) {
		Set<T> combinedClasspathConfigurations = Sets.newHashSet();
		Set<ConfigurationDescriptor<ClasspathResource>> configurationDescriptors = this.getConfigurationDescriptorResolver().resolve(input);
		for(ConfigurationDescriptor<ClasspathResource> configurationDescriptor : configurationDescriptors){
			try {
				combinedClasspathConfigurations.add(resolveClasspathPropertiesConfiguration(configurationDescriptor));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return combinedClasspathConfigurations;
	}

}
