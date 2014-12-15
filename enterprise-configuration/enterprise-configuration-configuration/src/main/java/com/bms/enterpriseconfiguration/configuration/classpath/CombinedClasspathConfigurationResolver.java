package com.bms.enterpriseconfiguration.configuration.classpath;

import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptor;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptorResolver;
import com.bms.enterpriseconfiguration.configuration.classpath.util.CommonsConfigurationUtil;
import com.bms.enterpriseconfiguration.core.CombinedResolutionTestResult;
import com.bms.enterpriseconfiguration.resources.ResourceOrdering;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.collect.Lists;

public class CombinedClasspathConfigurationResolver extends AbstractClasspathConfigurationResolver<CombinedClasspathConfiguration> {

	public CombinedClasspathConfigurationResolver(ConfigurationDescriptorResolver<ClasspathResource> configurationDescriptorResolver) {
		super(configurationDescriptorResolver);
		this.getPostresolutionActivities().add(new CombinedClasspathConfigurationImportHandler());
	}

	@Override
	protected CombinedClasspathConfiguration resolveClasspathPropertiesConfiguration(ConfigurationDescriptor<ClasspathResource> configurationDescriptor) throws ConfigurationException {
		CombinedClasspathConfiguration combinedClasspathConfiguration = new CombinedClasspathConfiguration();
		combinedClasspathConfiguration.setCombinedConfigurationDescriptor(configurationDescriptor);
		List<ClasspathResource> classpathResources = Lists.newArrayList(configurationDescriptor.getResources());
		Collections.sort(classpathResources, Collections.reverseOrder(new ResourceOrdering()));
		for(ClasspathResource classpathResource : classpathResources){
			combinedClasspathConfiguration.addConfiguration(CommonsConfigurationUtil.buildConfiguration(classpathResource));
		}
		return combinedClasspathConfiguration;
	}

	@Override
	public void handlePreresolutionTestResults(CombinedResolutionTestResult preresolutionTestResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePostresolutionTestResults(CombinedResolutionTestResult postresolutionTestResult) {
		// TODO Auto-generated method stub
		
	}

}
