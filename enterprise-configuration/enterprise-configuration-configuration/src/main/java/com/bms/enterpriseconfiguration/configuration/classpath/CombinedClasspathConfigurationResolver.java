package com.bms.enterpriseconfiguration.configuration.classpath;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptor;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptorResolver;
import com.bms.enterpriseconfiguration.configuration.classpath.annotation.CombinedClasspathConfigurationResolverPostresolutionActivity;
import com.bms.enterpriseconfiguration.configuration.classpath.annotation.CombinedClasspathConfigurationResolverPostresolutionTest;
import com.bms.enterpriseconfiguration.configuration.classpath.annotation.CombinedClasspathConfigurationResolverPreresolutionActivity;
import com.bms.enterpriseconfiguration.configuration.classpath.annotation.CombinedClasspathConfigurationResolverPreresolutionTest;
import com.bms.enterpriseconfiguration.configuration.classpath.util.CommonsConfigurationUtil;
import com.bms.enterpriseconfiguration.resources.ResourceOrdering;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class CombinedClasspathConfigurationResolver extends AbstractClasspathConfigurationResolver<CombinedClasspathConfiguration> {

	public CombinedClasspathConfigurationResolver(ConfigurationDescriptorResolver<ClasspathResource> configurationDescriptorResolver) {
		super(configurationDescriptorResolver);
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
	public Optional<Class<? extends Annotation>> getPreresolutionTestAnnotationClass(){
		return Optional.<Class<? extends Annotation>>of(CombinedClasspathConfigurationResolverPreresolutionTest.class);
	}
	
	@Override
	public Optional<Class<? extends Annotation>> getPostresolutionTestAnnotationClass(){
		return Optional.<Class<? extends Annotation>>of(CombinedClasspathConfigurationResolverPostresolutionTest.class);
	}
	
	@Override
	public Optional<Class<? extends Annotation>> getPreresolutionActivityAnnotationClass(){
		return Optional.<Class<? extends Annotation>>of(CombinedClasspathConfigurationResolverPreresolutionActivity.class);
	}
	
	@Override
	public Optional<Class<? extends Annotation>> getPostresolutionActivityAnnotationClass(){
		return Optional.<Class<? extends Annotation>>of(CombinedClasspathConfigurationResolverPostresolutionActivity.class);
	}

}
