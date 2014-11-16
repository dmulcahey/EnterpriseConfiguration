package com.bms.enterpriseconfiguration.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.junit.Test;

import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.bms.enterpriseconfiguration.resources.classpath.FilteredClasspathResourceResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.filter.ExtensionFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.NotFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.PathFilter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CombinedConfigurationDescriptorResolverTest {

	@Test
	public void testCombinedConfigurationDescriptorResolver(){
		FilteredClasspathResourceResourceProvider componentResourcesProvider = new FilteredClasspathResourceResourceProvider(300);
		componentResourcesProvider.add(new PathFilter("ComponentResources/Configuration"));
		componentResourcesProvider.add(new NotFilter(new PathFilter("EnvironmentOverrides")));
		componentResourcesProvider.add(new ExtensionFilter(".properties"));
		
		FilteredClasspathResourceResourceProvider environmentOverridesResourcesProvider = new FilteredClasspathResourceResourceProvider(400);
		environmentOverridesResourcesProvider.add(new PathFilter("ComponentResources/Configuration/EnvironmentOverrides/JUNIT"));
		environmentOverridesResourcesProvider.add(new ExtensionFilter(".properties"));
		
		FilteredClasspathResourceResourceProvider environmentResourcesProvider = new FilteredClasspathResourceResourceProvider(200);
		environmentResourcesProvider.add(new PathFilter("EnvironmentResources/JUNIT"));
		environmentResourcesProvider.add(new ExtensionFilter(".properties"));
		
		FilteredClasspathResourceResourceProvider sharedResourcesProvider = new FilteredClasspathResourceResourceProvider(100);
		sharedResourcesProvider.add(new PathFilter("SharedResources"));
		sharedResourcesProvider.add(new ExtensionFilter(".properties"));
		
		FilteredClasspathResourceResourceProvider secureResourcesProvider = new FilteredClasspathResourceResourceProvider(500, true);
		secureResourcesProvider.add(new PathFilter("SecureResources/JUNIT/Configuration"));
		secureResourcesProvider.add(new ExtensionFilter(".properties"));
		
		Set<FilteredClasspathResourceResourceProvider> resourceProviders = Sets.newHashSetWithExpectedSize(4);
		resourceProviders.add(componentResourcesProvider);
		resourceProviders.add(secureResourcesProvider);
		resourceProviders.add(sharedResourcesProvider);
		resourceProviders.add(environmentResourcesProvider);
		resourceProviders.add(environmentOverridesResourcesProvider);
		
		ConfigurationDescriptorResolver<ClasspathResource> combinedConfigurationDescriptorResolver = new ConfigurationDescriptorResolver<ClasspathResource>();
		
		Set<ConfigurationDescriptor<ClasspathResource>> configurationDescriptors =  combinedConfigurationDescriptorResolver.doResolution(resourceProviders);
		
		assertNotNull(configurationDescriptors);
		
		for(ConfigurationDescriptor<ClasspathResource> configurationDescriptor : configurationDescriptors){
			Logger.getAnonymousLogger().info("\n\n" + configurationDescriptor + "\n\n");
		}
		
	}
	
	@Test
	public void testStringSubstitutor(){
		Map<String,String> variables = Maps.newHashMap();
		variables.put("componentName", "Configuration");
		variables.put("environment", "JUNIT");
		String template = "ComponentResources/${componentName}/EnvironmentOverrides/${environment}";
		String afterSubstitution = StrSubstitutor.replace(template, variables);
		
		assertEquals("ComponentResources/Configuration/EnvironmentOverrides/JUNIT", afterSubstitution);
		
		Logger.getAnonymousLogger().info(template + " -> " + afterSubstitution);
		
	}
	
}
