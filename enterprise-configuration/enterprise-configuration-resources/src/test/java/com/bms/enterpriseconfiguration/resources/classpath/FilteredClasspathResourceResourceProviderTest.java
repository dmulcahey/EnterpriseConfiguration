package com.bms.enterpriseconfiguration.resources.classpath;

import java.util.logging.Logger;

import org.junit.Test;

import com.bms.enterpriseconfiguration.resources.classpath.filter.ExtensionFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.NotFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.PathFilter;

public class FilteredClasspathResourceResourceProviderTest {

	
	@Test
	public void testFilteredClasspathResourceResourceProvider(){
		FilteredClasspathResourceResourceProvider classpathResourceResourceProvider = new FilteredClasspathResourceResourceProvider();
		classpathResourceResourceProvider.setOrder(100);
		classpathResourceResourceProvider.add(new PathFilter("ComponentResources"));
		
		Logger.getAnonymousLogger().info(classpathResourceResourceProvider.toString());
		
		Logger.getAnonymousLogger().info(classpathResourceResourceProvider.getResources().toString());
		
		classpathResourceResourceProvider.add(new NotFilter(new PathFilter("EnvironmentOverrides")));
		Logger.getAnonymousLogger().info(classpathResourceResourceProvider.getResources().toString());
		
		classpathResourceResourceProvider.getResourceFilters().clear();
		classpathResourceResourceProvider.add(new PathFilter("ComponentResources"));
		classpathResourceResourceProvider.getResourceFilters().add(new NotFilter(new ExtensionFilter(".properties")));
		Logger.getAnonymousLogger().info(classpathResourceResourceProvider.getResources().toString());
	}
	
}
