package com.bms.enterpriseconfiguration.configuration.component;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.text.StrSubstitutor;

import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptorResolver;
import com.bms.enterpriseconfiguration.configuration.classpath.CombinedClasspathConfiguration;
import com.bms.enterpriseconfiguration.configuration.classpath.CombinedClasspathConfigurationResolver;
import com.bms.enterpriseconfiguration.configuration.component.annotation.ComponentConfigurationResolverPostresolutionActivity;
import com.bms.enterpriseconfiguration.configuration.component.annotation.ComponentConfigurationResolverPostresolutionTest;
import com.bms.enterpriseconfiguration.configuration.component.annotation.ComponentConfigurationResolverPreresolutionActivity;
import com.bms.enterpriseconfiguration.configuration.component.annotation.ComponentConfigurationResolverPreresolutionTest;
import com.bms.enterpriseconfiguration.core.AbstractResolver;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.bms.enterpriseconfiguration.resources.classpath.FilteredClasspathResourceResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.filter.ExtensionFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.NotFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.OrFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.PathFilter;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ComponentConfigurationResolver extends AbstractResolver<ComponentConfigurationResolver.Criteria, ComponentConfiguration>{
	public static final String COMPONENT_NAME = "componentName";
	public static final String ENVIRONMENT = "environment";
	public static final String ENVIRONMENT_RESOURCES = "EnvironmentResources";
	public static final String COMPONENT_RESOURCES = "ComponentResources";
	public static final String ENVIRONMENT_OVERRIDE_RESOURCES = "EnvironmentOverrides";
	public static final String SECURE_RESOURCES = "SecureResources";
	public static final String SHARED_RESOURCES = "SharedResources";
	
	private static final String SHARED_RESOURCES_LOCATOR = SHARED_RESOURCES + "/";
	private static final String ENVIRONMENT_OVERRIDES_RESOURCES_LOCATOR = ENVIRONMENT_OVERRIDE_RESOURCES + "/";
	private static final String COMPONENT_RESOURCES_LOCATOR_TEMPLATE = COMPONENT_RESOURCES + "/${componentName}/";
	private static final String ENVIRONMENT_RESOURCES_LOCATOR_TEMPLATE = ENVIRONMENT_RESOURCES + "/${environment}/";
	private static final String SECURE_RESOURCES_LOCATOR_TEMPLATE = SECURE_RESOURCES + "/${environment}/${componentName}/";
	private static final String ENVIRONMENT_OVERRIDES_RESOURCES_LOCATOR_TEMPLATE = COMPONENT_RESOURCES + "/${componentName}/" + ENVIRONMENT_OVERRIDE_RESOURCES + "/${environment}/";
	
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
	public Optional<Class<? extends Annotation>> getPreresolutionTestAnnotationClass(){
		return Optional.<Class<? extends Annotation>>of(ComponentConfigurationResolverPreresolutionTest.class);
	}
	
	@Override
	public Optional<Class<? extends Annotation>> getPostresolutionTestAnnotationClass(){
		return Optional.<Class<? extends Annotation>>of(ComponentConfigurationResolverPostresolutionTest.class);
	}
	
	@Override
	public Optional<Class<? extends Annotation>> getPreresolutionActivityAnnotationClass(){
		return Optional.<Class<? extends Annotation>>of(ComponentConfigurationResolverPreresolutionActivity.class);
	}
	
	@Override
	public Optional<Class<? extends Annotation>> getPostresolutionActivityAnnotationClass(){
		return Optional.<Class<? extends Annotation>>of(ComponentConfigurationResolverPostresolutionActivity.class);
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
			.withResourceFilter(new NotFilter(new PathFilter(ENVIRONMENT_OVERRIDES_RESOURCES_LOCATOR)))
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
			resources.put(resource.getResourceName().substring(SHARED_RESOURCES_LOCATOR.length()), resource);
		}
		
		String environmentResourcesLocator = StrSubstitutor.replace(ENVIRONMENT_RESOURCES_LOCATOR_TEMPLATE, variables);
		FilteredClasspathResourceResourceProvider environmentResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(200)
			.withResourceFilter(new PathFilter(environmentResourcesLocator))
			.withResourceFilter(extensionFilter)
			.build();
		
		for(ClasspathResource resource : environmentResourcesProvider.getResources()){
			resources.put(resource.getResourceName().substring(environmentResourcesLocator.length()), resource);
		}
		
		String componentResourceLocator = StrSubstitutor.replace(COMPONENT_RESOURCES_LOCATOR_TEMPLATE, variables);
		FilteredClasspathResourceResourceProvider componentResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(300)
			.withResourceFilter(new PathFilter(componentResourceLocator))
			.withResourceFilter(new NotFilter(new PathFilter(ENVIRONMENT_OVERRIDES_RESOURCES_LOCATOR)))
			.withResourceFilter(extensionFilter)
			.build();
		
		for(ClasspathResource resource : componentResourcesProvider.getResources()){
			resources.put(resource.getResourceName().substring(componentResourceLocator.length()), resource);
		}
		
		String environmentOverrideResourcesLocator = StrSubstitutor.replace(ENVIRONMENT_OVERRIDES_RESOURCES_LOCATOR_TEMPLATE, variables);
		FilteredClasspathResourceResourceProvider environmentOverridesResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(400)
			.withResourceFilter(new PathFilter(environmentOverrideResourcesLocator))
			.withResourceFilter(extensionFilter)
			.build();
		
		for(ClasspathResource resource : environmentOverridesResourcesProvider.getResources()){
			resources.put(resource.getResourceName().substring(environmentOverrideResourcesLocator.length()), resource);
		}
		
		String secureResourcesLocator = StrSubstitutor.replace(SECURE_RESOURCES_LOCATOR_TEMPLATE, variables);
		FilteredClasspathResourceResourceProvider secureResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(500)
			.secure(true)
			.withResourceFilter(new PathFilter(secureResourcesLocator))
			.withResourceFilter(extensionFilter)
			.build();
		
		for(ClasspathResource resource : secureResourcesProvider.getResources()){
			resources.put(resource.getResourceName().substring(secureResourcesLocator.length()), resource);
		}
		
		return resources;
	}
	
	private static Map<String, String> buildVariables(Criteria criteria){
		Map<String,String> variables = Maps.newHashMapWithExpectedSize(2);
		variables.put(COMPONENT_NAME, criteria.getComponentName());
		variables.put(ENVIRONMENT, criteria.getEnvironment());
		return variables;
	}
	
	//TODO find a better name for this
	public static class Criteria {
		private final String componentName;
		private final String environment;
		
		public Criteria(String componentName, String environment) {
			super();
			checkNotNull(componentName, "componentName is required");
			checkNotNull(environment, "environment is required");
			this.componentName = componentName;
			this.environment = environment;
		}

		public String getComponentName() {
			return componentName;
		}
		
		public String getEnvironment() {
			return environment;
		}
		
		@Override
		public String toString() {
			return "Criteria [componentName=" + componentName
					+ ", environment=" + environment + "]";
		}
		
	}
	
}