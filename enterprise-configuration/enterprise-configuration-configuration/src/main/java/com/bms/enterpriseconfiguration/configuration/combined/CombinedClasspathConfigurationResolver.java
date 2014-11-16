package com.bms.enterpriseconfiguration.configuration.combined;

import java.util.Set;

import com.bms.enterpriseconfiguration.configuration.AbstractConfigurationResolver;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptor;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptorResolver;
import com.bms.enterpriseconfiguration.resources.ResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.collect.Sets;

public class CombinedClasspathConfigurationResolver extends AbstractConfigurationResolver<ClasspathResource>{

	public CombinedClasspathConfigurationResolver(ConfigurationDescriptorResolver<ClasspathResource> configurationDescriptorResolver) {
		super(configurationDescriptorResolver);
	}

	@Override
	protected Set<CombinedClasspathConfiguration<ClasspathResource>> doResolution(Set<? extends ResourceProvider<Set<ClasspathResource>>> input) {
		Set<CombinedClasspathConfiguration<ClasspathResource>> combinedClasspathConfigurations = Sets.newHashSet();
		Set<ConfigurationDescriptor<ClasspathResource>> configurationDescriptors = this.getConfigurationDescriptorResolver().resolve(input);
		for(ConfigurationDescriptor<ClasspathResource> configurationDescriptor : configurationDescriptors){
			combinedClasspathConfigurations.add(resolveCombinedClasspathConfiguration(configurationDescriptor));
		}
		return combinedClasspathConfigurations;
	}
	
	private CombinedClasspathConfiguration<ClasspathResource> resolveCombinedClasspathConfiguration(ConfigurationDescriptor<ClasspathResource> configurationDescriptor){
		return null;
	}

}
