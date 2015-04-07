package com.bms.enterpriseconfiguration.resources.classpath.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Set;

import org.junit.Test;

import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.bms.enterpriseconfiguration.resources.classpath.FilteredClasspathResourceResourceProvider;

public class DirectoryFilterTest {

	@Test
	public void testAcceptPath(){
		ClasspathResource resource = getResource();
		URL url = resource.getURL();
		String urlString = url.toExternalForm();
		String componentResourcesDirectory = urlString.substring(0, urlString.indexOf("ComponentResources") + "ComponentResources".length());
		assertTrue(new DirectoryFilter().accept(componentResourcesDirectory));
	}
	
	@Test
	public void testRejectPath(){
		assertFalse(new DirectoryFilter().accept(getResource().getURL().toExternalForm()));
	}
	
	private ClasspathResource getResource(){
		FilteredClasspathResourceResourceProvider resourcesProvider = FilteredClasspathResourceResourceProvider.builder()
				.withResourceFilter(new PathFilter("ComponentResources/ConfigurationCore/junk.properties"))
				.build();
		Set<ClasspathResource> resources = resourcesProvider.getResources();
		return resources.iterator().next();
	}
	
}
