package com.bms.enterpriseconfiguration.resources.classpath;

import java.util.logging.Logger;

import org.junit.Test;

import com.bms.enterpriseconfiguration.resources.classpath.filter.ExtensionFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.NotFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.PathFilter;

public class FilteredClasspathResourceResourceProviderTest {

	
	@Test
	public void testFilteredClasspathResourceResourceProvider(){
		FilteredClasspathResourceResourceProvider classpathResourceResourceProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(100)
			.withResourceFilter(new PathFilter("ComponentResources"))
			.build();
		
		Logger.getAnonymousLogger().info(classpathResourceResourceProvider.toString());
		
		Logger.getAnonymousLogger().info(classpathResourceResourceProvider.getResources().toString());
		
		classpathResourceResourceProvider = FilteredClasspathResourceResourceProvider.builder()
		.order(100)
		.withResourceFilter(new PathFilter("ComponentResources"))
		.withResourceFilter(new NotFilter(new PathFilter("EnvironmentOverrides")))
		.build();
		
		Logger.getAnonymousLogger().info(classpathResourceResourceProvider.getResources().toString());
		
		classpathResourceResourceProvider = FilteredClasspathResourceResourceProvider.builder()
		.order(100)
		.withResourceFilter(new PathFilter("ComponentResources"))
		.withResourceFilter(new NotFilter(ExtensionFilter.PROPERTIES_FILTER))
		.build();
		
		Logger.getAnonymousLogger().info(classpathResourceResourceProvider.getResources().toString());
	}
	
}
