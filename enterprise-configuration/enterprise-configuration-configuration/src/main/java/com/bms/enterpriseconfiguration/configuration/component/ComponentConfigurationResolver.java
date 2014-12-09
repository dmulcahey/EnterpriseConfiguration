package com.bms.enterpriseconfiguration.configuration.component;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.text.StrSubstitutor;

import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptorResolver;
import com.bms.enterpriseconfiguration.configuration.classpath.CombinedClasspathConfiguration;
import com.bms.enterpriseconfiguration.configuration.classpath.CombinedClasspathConfigurationResolver;
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
	
	@Override
	protected ComponentConfiguration doResolution(Criteria criteria) {
		ComponentConfiguration componentConfiguration = new ComponentConfiguration();
		componentConfiguration.setName(criteria.getComponentName());
		Set<FilteredClasspathResourceResourceProvider> resourceProviders = buildResourceProviders(criteria);
		ConfigurationDescriptorResolver<ClasspathResource> combinedConfigurationDescriptorResolver = new ConfigurationDescriptorResolver<ClasspathResource>();
		CombinedClasspathConfigurationResolver combinedClasspathConfigurationResolver = new CombinedClasspathConfigurationResolver(combinedConfigurationDescriptorResolver);
		Set<CombinedClasspathConfiguration> combinedClasspathConfigurations = combinedClasspathConfigurationResolver.resolve(resourceProviders);
		for(CombinedClasspathConfiguration configuration : combinedClasspathConfigurations){
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
		// TODO need to implement duplicate configuration names because we chop off the extension. 
		OrFilter extensionFilter = new OrFilter(ExtensionFilter.PROPERTIES_FILTER, ExtensionFilter.XML_FILTER);
		
		FilteredClasspathResourceResourceProvider sharedResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(100)
			.withResourceFilter(new PathFilter(SHARED_RESOURCES_LOCATOR))
			.withResourceFilter(extensionFilter)
			.build();
		resourceProviders.add(sharedResourcesProvider);
		
		FilteredClasspathResourceResourceProvider environmentResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(200)
			.withResourceFilter(new PathFilter(StrSubstitutor.replace(ENVIRONMENT_RESOURCES_LOCATOR_TEMPLATE, variables)))
			.withResourceFilter(extensionFilter)
			.build();
		resourceProviders.add(environmentResourcesProvider);
		
		FilteredClasspathResourceResourceProvider componentResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(300)
			.withResourceFilter(new PathFilter(StrSubstitutor.replace(COMPONENT_RESOURCES_LOCATOR_TEMPLATE, variables)))
			.withResourceFilter(new NotFilter(new PathFilter("EnvironmentOverrides")))
			.withResourceFilter(extensionFilter)
			.build();
		resourceProviders.add(componentResourcesProvider);
		
		FilteredClasspathResourceResourceProvider environmentOverridesResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(400)
			.withResourceFilter(new PathFilter(StrSubstitutor.replace(ENVIRONMENT_OVERRIDES_RESOURCES_LOCATOR_TEMPLATE, variables)))
			.withResourceFilter(extensionFilter)
			.build();
		resourceProviders.add(environmentOverridesResourcesProvider);
		
		FilteredClasspathResourceResourceProvider secureResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(500)
			.secure(true)
			.withResourceFilter(new PathFilter(StrSubstitutor.replace(SECURE_RESOURCES_LOCATOR_TEMPLATE, variables)))
			.withResourceFilter(extensionFilter)
			.build();
		resourceProviders.add(secureResourcesProvider);
		
		return resourceProviders;
	}
	
	private static Map<String, ClasspathResource> resolveResources(Criteria criteria){
		Map<String,String> variables = buildVariables(criteria);
		Map<String, ClasspathResource> resources = Maps.newHashMap();
		NotFilter extensionFilter = new NotFilter(ExtensionFilter.CLASS_FILTER);
		
		FilteredClasspathResourceResourceProvider sharedResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(100)
			.withResourceFilter(new PathFilter(SHARED_RESOURCES_LOCATOR))
			.withResourceFilter(extensionFilter)
			.build();
		
		for(ClasspathResource resource : sharedResourcesProvider.getResources()){
			resources.put(resource.getResourceName().substring(SHARED_RESOURCES_LOCATOR.length()+1), resource);
		}
		
		String environmentResourcesLocator = StrSubstitutor.replace(ENVIRONMENT_RESOURCES_LOCATOR_TEMPLATE, variables);
		FilteredClasspathResourceResourceProvider environmentResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(200)
			.withResourceFilter(new PathFilter(environmentResourcesLocator))
			.withResourceFilter(extensionFilter)
			.build();
		
		for(ClasspathResource resource : environmentResourcesProvider.getResources()){
			resources.put(resource.getResourceName().substring(environmentResourcesLocator.length()+1), resource);
		}
		
		String componentResourceLocator = StrSubstitutor.replace(COMPONENT_RESOURCES_LOCATOR_TEMPLATE, variables);
		FilteredClasspathResourceResourceProvider componentResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(300)
			.withResourceFilter(new PathFilter(componentResourceLocator))
			.withResourceFilter(new NotFilter(new PathFilter("EnvironmentOverrides")))
			.withResourceFilter(extensionFilter)
			.build();
		
		for(ClasspathResource resource : componentResourcesProvider.getResources()){
			resources.put(resource.getResourceName().substring(componentResourceLocator.length()+1), resource);
		}
		
		String environmentOverrideResourcesLocator = StrSubstitutor.replace(ENVIRONMENT_OVERRIDES_RESOURCES_LOCATOR_TEMPLATE, variables);
		FilteredClasspathResourceResourceProvider environmentOverridesResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(400)
			.withResourceFilter(new PathFilter(environmentOverrideResourcesLocator))
			.withResourceFilter(extensionFilter)
			.build();
		
		for(ClasspathResource resource : environmentOverridesResourcesProvider.getResources()){
			resources.put(resource.getResourceName().substring(environmentOverrideResourcesLocator.length()+1), resource);
		}
		
		String secureResourcesLocator = StrSubstitutor.replace(SECURE_RESOURCES_LOCATOR_TEMPLATE, variables);
		FilteredClasspathResourceResourceProvider secureResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(500)
			.secure(true)
			.withResourceFilter(new PathFilter(secureResourcesLocator))
			.withResourceFilter(extensionFilter)
			.build();
		
		for(ClasspathResource resource : secureResourcesProvider.getResources()){
			resources.put(resource.getResourceName().substring(secureResourcesLocator.length()+1), resource);
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
