package com.bms.enterpriseconfiguration.configuration.properties;

import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptor;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptorResolver;
import com.bms.enterpriseconfiguration.configuration.properties.util.PropertiesConfigurationUtil;
import com.bms.enterpriseconfiguration.resources.ResourceOrdering;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.collect.Lists;

public class CombinedClasspathPropertiesConfigurationResolver extends AbstractClasspathPropertiesConfigurationResolver<CombinedClasspathPropertiesConfiguration> {

	public CombinedClasspathPropertiesConfigurationResolver(ConfigurationDescriptorResolver<ClasspathResource> configurationDescriptorResolver) {
		super(configurationDescriptorResolver);
		this.getPostresolutionResolutionActivities().add(new CombinedClasspathPropertiesConfigurationImportHandler());
	}

	@Override
	protected CombinedClasspathPropertiesConfiguration resolveClasspathPropertiesConfiguration(ConfigurationDescriptor<ClasspathResource> configurationDescriptor) throws ConfigurationException {
		CombinedClasspathPropertiesConfiguration combinedClasspathConfiguration = new CombinedClasspathPropertiesConfiguration();
		combinedClasspathConfiguration.setCombinedConfigurationDescriptor(configurationDescriptor);
		List<ClasspathResource> classpathResources = Lists.newArrayList(configurationDescriptor.getResources());
		Collections.sort(classpathResources, Collections.reverseOrder(new ResourceOrdering()));
		for(ClasspathResource classpathResource : classpathResources){
			combinedClasspathConfiguration.addConfiguration(PropertiesConfigurationUtil.buildPropertiesConfiguration(classpathResource));
		}
		return combinedClasspathConfiguration;
	}

}
