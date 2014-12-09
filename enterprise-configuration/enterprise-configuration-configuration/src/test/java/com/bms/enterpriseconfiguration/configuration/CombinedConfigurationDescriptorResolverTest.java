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
		FilteredClasspathResourceResourceProvider componentResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(300)
			.withResourceFilter(new PathFilter("ComponentResources/Configuration"))
			.withResourceFilter(new NotFilter(new PathFilter("EnvironmentOverrides")))
			.withResourceFilter(new ExtensionFilter(".properties"))
			.build();
		
		FilteredClasspathResourceResourceProvider environmentOverridesResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(400)
			.withResourceFilter(new PathFilter("ComponentResources/Configuration/EnvironmentOverrides/JUNIT"))
			.withResourceFilter(new ExtensionFilter(".properties"))
			.build();
		
		FilteredClasspathResourceResourceProvider environmentResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(200)
			.withResourceFilter(new PathFilter("EnvironmentResources/JUNIT"))
			.withResourceFilter(new ExtensionFilter(".properties"))
			.build();
		
		FilteredClasspathResourceResourceProvider sharedResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(100)
			.withResourceFilter(new PathFilter("SharedResources"))
			.withResourceFilter(new ExtensionFilter(".properties"))
			.build();
		
		FilteredClasspathResourceResourceProvider secureResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(500)
			.secure(true)
			.withResourceFilter(new PathFilter("SecureResources/JUNIT/Configuration"))
			.withResourceFilter(new ExtensionFilter(".properties"))
			.build();
		
		Set<FilteredClasspathResourceResourceProvider> resourceProviders = Sets.newHashSetWithExpectedSize(4);
		resourceProviders.add(componentResourcesProvider);
		resourceProviders.add(secureResourcesProvider);
		resourceProviders.add(sharedResourcesProvider);
		resourceProviders.add(environmentResourcesProvider);
		resourceProviders.add(environmentOverridesResourcesProvider);
		
		ConfigurationDescriptorResolver<ClasspathResource> combinedConfigurationDescriptorResolver = new ConfigurationDescriptorResolver<ClasspathResource>();
		
		Set<ConfigurationDescriptor<ClasspathResource>> configurationDescriptors =  combinedConfigurationDescriptorResolver.resolve(resourceProviders);
		
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
