package com.bms.enterpriseconfiguration.configuration;

import static org.junit.Assert.assertNotNull;

import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.bms.enterpriseconfiguration.resources.classpath.FilteredClasspathResourceResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.filter.ExtensionFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.NotFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.PathFilter;
import com.google.common.collect.Sets;

public class CombinedConfigurationDescriptorResolverTest {

	@Test
	public void testCombinedConfigurationDescriptorResolver(){
		FilteredClasspathResourceResourceProvider componentResourcesProvider = new FilteredClasspathResourceResourceProvider(300);
		componentResourcesProvider.add(new PathFilter("ComponentResources/Configuration"));
		componentResourcesProvider.add(new NotFilter(new PathFilter("EnvironmentOverrides")));
		componentResourcesProvider.add(new ExtensionFilter(".properties"));
		
		FilteredClasspathResourceResourceProvider environmentResourcesProvider = new FilteredClasspathResourceResourceProvider(200);
		environmentResourcesProvider.add(new PathFilter("EnvironmentResources/JUNIT"));
		environmentResourcesProvider.add(new ExtensionFilter(".properties"));
		
		FilteredClasspathResourceResourceProvider sharedResourcesProvider = new FilteredClasspathResourceResourceProvider(100);
		sharedResourcesProvider.add(new PathFilter("SharedResources"));
		sharedResourcesProvider.add(new ExtensionFilter(".properties"));
		
		FilteredClasspathResourceResourceProvider secureResourcesProvider = new FilteredClasspathResourceResourceProvider(400, true);
		secureResourcesProvider.add(new PathFilter("SecureResources/JUNIT/Configuration"));
		secureResourcesProvider.add(new ExtensionFilter(".properties"));
		
		Set<FilteredClasspathResourceResourceProvider> resourceProviders = Sets.newHashSetWithExpectedSize(4);
		resourceProviders.add(componentResourcesProvider);
		resourceProviders.add(secureResourcesProvider);
		resourceProviders.add(sharedResourcesProvider);
		resourceProviders.add(environmentResourcesProvider);
		
		CombinedConfigurationDescriptorResolver<ClasspathResource> combinedConfigurationDescriptorResolver = new CombinedConfigurationDescriptorResolver<ClasspathResource>();
		
		Set<CombinedConfigurationDescriptor<ClasspathResource>> configurationDescriptors =  combinedConfigurationDescriptorResolver.doResolution(resourceProviders);
		
		assertNotNull(configurationDescriptors);
		
		Logger.getAnonymousLogger().info(configurationDescriptors.toString());
		
	}
	
}
