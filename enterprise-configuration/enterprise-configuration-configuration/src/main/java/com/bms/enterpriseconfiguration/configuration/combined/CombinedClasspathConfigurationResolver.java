package com.bms.enterpriseconfiguration.configuration.combined;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.bms.enterpriseconfiguration.configuration.AbstractConfigurationResolver;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptor;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptorResolver;
import com.bms.enterpriseconfiguration.configuration.combined.util.PropertiesConfigurationUtil;
import com.bms.enterpriseconfiguration.resources.ResourceOrdering;
import com.bms.enterpriseconfiguration.resources.ResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.collect.Lists;
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
			try {
				combinedClasspathConfigurations.add(resolveCombinedClasspathConfiguration(configurationDescriptor));
			} catch (ConfigurationException e) {
				throw new RuntimeException(e);
			}
		}
		return combinedClasspathConfigurations;
	}
	
	private CombinedClasspathConfiguration<ClasspathResource> resolveCombinedClasspathConfiguration(ConfigurationDescriptor<ClasspathResource> configurationDescriptor) throws ConfigurationException{
		CombinedClasspathConfiguration<ClasspathResource> combinedClasspathConfiguration = new CombinedClasspathConfiguration<ClasspathResource>();
		List<ClasspathResource> classpathResources = Lists.newArrayList(configurationDescriptor.getResources());
		Collections.sort(classpathResources, Collections.reverseOrder(new ResourceOrdering()));
		for(ClasspathResource classpathResource : classpathResources){
			combinedClasspathConfiguration.addConfiguration(PropertiesConfigurationUtil.buildPropertiesConfiguration(classpathResource));
		}
		return combinedClasspathConfiguration;
	}

}
