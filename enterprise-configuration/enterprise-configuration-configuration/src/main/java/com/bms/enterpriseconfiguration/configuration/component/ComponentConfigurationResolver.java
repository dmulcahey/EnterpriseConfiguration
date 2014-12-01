package com.bms.enterpriseconfiguration.configuration.component;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.text.StrSubstitutor;

import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptorResolver;
import com.bms.enterpriseconfiguration.configuration.properties.CombinedClasspathPropertiesConfiguration;
import com.bms.enterpriseconfiguration.configuration.properties.CombinedClasspathPropertiesConfigurationResolver;
import com.bms.enterpriseconfiguration.core.AbstractResolver;
import com.bms.enterpriseconfiguration.core.CombinedResolutionTestResult;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.bms.enterpriseconfiguration.resources.classpath.FilteredClasspathResourceResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.filter.ExtensionFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.NotFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.OrFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.PathFilter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ComponentConfigurationResolver extends AbstractResolver<ComponentConfigurationResolver.Criteria, ComponentConfiguration>{

	private static final String SHARED_RESOURCES_LOCATOR = "SharedResources";
	private static final String COMPONENT_RESOURCES_LOCATOR_TEMPLATE = "ComponentResources/${componentName}";
	private static final String ENVIRONMENT_RESOURCES_LOCATOR_TEMPLATE = "EnvironmentResources/${environment}";
	private static final String SECURE_RESOURCES_LOCATOR_TEMPLATE = "SecureResources/${environment}/${componentName}";
	private static final String ENVIRONMENT_OVERRIDES_RESOURCES_LOCATOR_TEMPLATE = "ComponentResources/${componentName}/EnvironmentOverrides/${environment}";
	private static final String PROPERTIES_EXTENSION = ".properties";
	
	@Override
	protected ComponentConfiguration doResolution(Criteria criteria) {
		ComponentConfiguration componentConfiguration = new ComponentConfiguration();
		componentConfiguration.setName(criteria.getComponentName());
		Set<FilteredClasspathResourceResourceProvider> resourceProviders = buildResourceProviders(criteria);
		ConfigurationDescriptorResolver<ClasspathResource> combinedConfigurationDescriptorResolver = new ConfigurationDescriptorResolver<ClasspathResource>();
		CombinedClasspathPropertiesConfigurationResolver combinedClasspathConfigurationResolver = new CombinedClasspathPropertiesConfigurationResolver(combinedConfigurationDescriptorResolver);
		Set<CombinedClasspathPropertiesConfiguration> combinedClasspathConfigurations = combinedClasspathConfigurationResolver.resolve(resourceProviders);
		for(CombinedClasspathPropertiesConfiguration configuration : combinedClasspathConfigurations){
			componentConfiguration.getConfigurations().put(configuration.getConfigurationDescriptor().getName(), configuration);
		}
		componentConfiguration.getResources().putAll(resolveResources(criteria));
		return componentConfiguration;
	}

	@Override
	protected void handlePreresolutionTestResults(CombinedResolutionTestResult preresolutionTestResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handlePostresolutionTestResults(CombinedResolutionTestResult postresolutionTestResult) {
		// TODO Auto-generated method stub
		
	}
	
	private static Set<FilteredClasspathResourceResourceProvider> buildResourceProviders(Criteria criteria){
		Set<FilteredClasspathResourceResourceProvider> resourceProviders = Sets.newHashSetWithExpectedSize(5);
		
		Map<String,String> variables = buildVariables(criteria);
		
		FilteredClasspathResourceResourceProvider sharedResourcesProvider = new FilteredClasspathResourceResourceProvider(100);
		sharedResourcesProvider.add(new PathFilter(SHARED_RESOURCES_LOCATOR));
		sharedResourcesProvider.add(new ExtensionFilter(PROPERTIES_EXTENSION));
		resourceProviders.add(sharedResourcesProvider);
		
		FilteredClasspathResourceResourceProvider environmentResourcesProvider = new FilteredClasspathResourceResourceProvider(200);
		environmentResourcesProvider.add(new PathFilter(StrSubstitutor.replace(ENVIRONMENT_RESOURCES_LOCATOR_TEMPLATE, variables)));
		environmentResourcesProvider.add(new ExtensionFilter(PROPERTIES_EXTENSION));
		resourceProviders.add(environmentResourcesProvider);
		
		FilteredClasspathResourceResourceProvider componentResourcesProvider = new FilteredClasspathResourceResourceProvider(300);
		componentResourcesProvider.add(new PathFilter(StrSubstitutor.replace(COMPONENT_RESOURCES_LOCATOR_TEMPLATE, variables)));
		componentResourcesProvider.add(new NotFilter(new PathFilter("EnvironmentOverrides")));
		componentResourcesProvider.add(new ExtensionFilter(PROPERTIES_EXTENSION));
		resourceProviders.add(componentResourcesProvider);
		
		FilteredClasspathResourceResourceProvider environmentOverridesResourcesProvider = new FilteredClasspathResourceResourceProvider(400);
		environmentOverridesResourcesProvider.add(new PathFilter(StrSubstitutor.replace(ENVIRONMENT_OVERRIDES_RESOURCES_LOCATOR_TEMPLATE, variables)));
		environmentOverridesResourcesProvider.add(new ExtensionFilter(PROPERTIES_EXTENSION));
		resourceProviders.add(environmentOverridesResourcesProvider);
		
		FilteredClasspathResourceResourceProvider secureResourcesProvider = new FilteredClasspathResourceResourceProvider(500, true);
		secureResourcesProvider.add(new PathFilter(StrSubstitutor.replace(SECURE_RESOURCES_LOCATOR_TEMPLATE, variables)));
		secureResourcesProvider.add(new ExtensionFilter(PROPERTIES_EXTENSION));
		resourceProviders.add(secureResourcesProvider);
		
		return resourceProviders;
	}
	
	private static Map<String, ClasspathResource> resolveResources(Criteria criteria){
		FilteredClasspathResourceResourceProvider classpathResourcesProvider = new FilteredClasspathResourceResourceProvider(0, false);
		Map<String,String> variables = buildVariables(criteria);
		String componentResourceLocator = StrSubstitutor.replace(COMPONENT_RESOURCES_LOCATOR_TEMPLATE, variables);
		classpathResourcesProvider.add(new PathFilter(componentResourceLocator));
		classpathResourcesProvider.add(new NotFilter(new OrFilter(new ExtensionFilter(PROPERTIES_EXTENSION), new ExtensionFilter(".class"))));
		Map<String, ClasspathResource> resources = Maps.newHashMap();
		for(ClasspathResource resource : classpathResourcesProvider.getResources()){
			resources.put(resource.getResourceName().substring(componentResourceLocator.length()+1), resource);
		}
		return resources;
	}
	
	private static Map<String, String> buildVariables(Criteria criteria){
		Map<String,String> variables = Maps.newHashMapWithExpectedSize(2);
		variables.put("componentName", criteria.getComponentName());
		variables.put("environment", criteria.getEnvironment());
		return variables;
	}
	
	//TODO find a better name for this
	public static class Criteria {
		private String componentName;
		private String environment;
		
		public String getComponentName() {
			return componentName;
		}
		
		public void setComponentName(String componentName) {
			this.componentName = componentName;
		}
		
		public String getEnvironment() {
			return environment;
		}
		
		public void setEnvironment(String environment) {
			this.environment = environment;
		}
		
		@Override
		public String toString() {
			return "Criteria [componentName=" + componentName
					+ ", environment=" + environment + "]";
		}
		
	}
	
}
