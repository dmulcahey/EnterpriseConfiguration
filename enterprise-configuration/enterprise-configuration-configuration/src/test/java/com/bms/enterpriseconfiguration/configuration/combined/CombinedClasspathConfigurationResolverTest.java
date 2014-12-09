package com.bms.enterpriseconfiguration.configuration.combined;

import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptorResolver;
import com.bms.enterpriseconfiguration.configuration.classpath.ClasspathConfiguration;
import com.bms.enterpriseconfiguration.configuration.classpath.CombinedClasspathConfiguration;
import com.bms.enterpriseconfiguration.configuration.classpath.CombinedClasspathConfigurationResolver;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.bms.enterpriseconfiguration.resources.classpath.FilteredClasspathResourceResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.filter.ExtensionFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.NotFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.PathFilter;
import com.google.common.collect.Sets;

public class CombinedClasspathConfigurationResolverTest {

	
	@Test
	public void testCombinedClasspathConfigurationResolver(){
		FilteredClasspathResourceResourceProvider componentResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(300)
			.withResourceFilter(new PathFilter("ComponentResources/Configuration"))
			.withResourceFilter(new NotFilter(new PathFilter("EnvironmentOverrides")))
			.withResourceFilter(ExtensionFilter.PROPERTIES_FILTER)
			.build();
		
		FilteredClasspathResourceResourceProvider environmentOverridesResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(400)
			.withResourceFilter(new PathFilter("ComponentResources/Configuration/EnvironmentOverrides/JUNIT"))
			.withResourceFilter(ExtensionFilter.PROPERTIES_FILTER)
			.build();
		
		FilteredClasspathResourceResourceProvider environmentResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(200)
			.withResourceFilter(new PathFilter("EnvironmentResources/JUNIT"))
			.withResourceFilter(ExtensionFilter.PROPERTIES_FILTER)
			.build();
		
		FilteredClasspathResourceResourceProvider sharedResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(100)
			.withResourceFilter(new PathFilter("SharedResources"))
			.withResourceFilter(ExtensionFilter.PROPERTIES_FILTER)
			.build();
		
		FilteredClasspathResourceResourceProvider secureResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(500)
			.secure(true)
			.withResourceFilter(new PathFilter("SecureResources/JUNIT/Configuration"))
			.withResourceFilter(ExtensionFilter.PROPERTIES_FILTER)
			.build();
		
		Set<FilteredClasspathResourceResourceProvider> resourceProviders = Sets.newHashSetWithExpectedSize(4);
		resourceProviders.add(componentResourcesProvider);
		resourceProviders.add(secureResourcesProvider);
		resourceProviders.add(sharedResourcesProvider);
		resourceProviders.add(environmentResourcesProvider);
		resourceProviders.add(environmentOverridesResourcesProvider);
		
		ConfigurationDescriptorResolver<ClasspathResource> combinedConfigurationDescriptorResolver = new ConfigurationDescriptorResolver<ClasspathResource>();
		
		CombinedClasspathConfigurationResolver combinedClasspathConfigurationResolver = new CombinedClasspathConfigurationResolver(combinedConfigurationDescriptorResolver);
		
		Set<CombinedClasspathConfiguration> combinedClasspathConfigurations = combinedClasspathConfigurationResolver.resolve(resourceProviders);
		
		for(ClasspathConfiguration combinedClasspathConfiguration : combinedClasspathConfigurations){
			Logger.getAnonymousLogger().info(combinedClasspathConfiguration.toString());
		}
	}
	
}
