package com.bms.enterpriseconfiguration.configuration.component.spring;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bms.enterpriseconfiguration.configuration.Configuration;
import com.bms.enterpriseconfiguration.configuration.component.spring.annotation.ClasspathResource;
import com.bms.enterpriseconfiguration.configuration.component.spring.annotation.CombinedClasspathConfiguration;

@Component
@Scope("prototype")
public class BeanWithInjectedConfiguration {

	@CombinedClasspathConfiguration(componentName="Configuration", name="junk", environment="JUNIT")
	private Configuration<?> configuration;
	
	@ClasspathResource(componentName="Configuration", name="junkXML.xml", environment="JUNIT")
	private com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource resource;

	public Configuration<?> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration<?> configuration) {
		this.configuration = configuration;
	}

	public com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource getResource() {
		return resource;
	}

	public void setResource(
			com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "BeanWithInjectedConfiguration [configuration=" + configuration
				+ ", resource=" + resource + "]";
	}

}
