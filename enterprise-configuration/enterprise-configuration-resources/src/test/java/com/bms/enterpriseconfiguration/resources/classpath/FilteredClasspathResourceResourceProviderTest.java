package com.bms.enterpriseconfiguration.resources.classpath;

import java.util.logging.Logger;

import org.junit.Test;

import com.bms.enterpriseconfiguration.resources.classpath.filter.PathFilter;

public class FilteredClasspathResourceResourceProviderTest {

	
	@Test
	public void testFilteredClasspathResourceResourceProvider(){
		FilteredClasspathResourceResourceProvider classpathResourceResourceProvider = new FilteredClasspathResourceResourceProvider();
		classpathResourceResourceProvider.setClasspathResourceSource(new ClasspathResourceResourceProvider());
		classpathResourceResourceProvider.setOrder(100);
		classpathResourceResourceProvider.add(new PathFilter("ComponentResources"));
		//classpathResourceProvider.add(new NotFilter(new PathFilter("EnvironmentOverrides")));
		
		Logger.getAnonymousLogger().info(classpathResourceResourceProvider.toString());
		
		Logger.getAnonymousLogger().info(classpathResourceResourceProvider.getResources().toString());
	}
	
}
