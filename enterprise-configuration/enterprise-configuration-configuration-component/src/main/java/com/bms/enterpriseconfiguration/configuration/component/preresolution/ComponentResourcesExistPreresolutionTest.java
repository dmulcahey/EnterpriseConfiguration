package com.bms.enterpriseconfiguration.configuration.component.preresolution;

import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;

import com.bms.enterpriseconfiguration.configuration.component.ComponentConfigurationResolver;
import com.bms.enterpriseconfiguration.configuration.component.ComponentConfigurationResolver.Criteria;
import com.bms.enterpriseconfiguration.configuration.component.annotation.ComponentConfigurationResolverPreresolutionTest;
import com.bms.enterpriseconfiguration.core.ResolutionTest;
import com.bms.enterpriseconfiguration.core.ResolutionTestResult;
import com.bms.enterpriseconfiguration.resources.classpath.FilteredClasspathResourceResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.filter.ExtensionFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.NotFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.OrFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.PathFilter;

@ComponentConfigurationResolverPreresolutionTest
public class ComponentResourcesExistPreresolutionTest implements ResolutionTest<Criteria> {

	@Override
	public ResolutionTestResult execute(Criteria input) {
		ResolutionTestResult result = new ResolutionTestResult();
		Map<String,String> variables = ComponentConfigurationResolver.buildVariables(input);
		OrFilter extensionFilter = new OrFilter(ExtensionFilter.PROPERTIES_FILTER, ExtensionFilter.XML_FILTER);
		FilteredClasspathResourceResourceProvider componentResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
				.order(300)
				.withResourceFilter(new PathFilter(StrSubstitutor.replace(ComponentConfigurationResolver.COMPONENT_RESOURCES_LOCATOR_TEMPLATE, variables)))
				.withResourceFilter(new NotFilter(new PathFilter(ComponentConfigurationResolver.ENVIRONMENT_OVERRIDES_RESOURCES_LOCATOR)))
				.withResourceFilter(extensionFilter)
				.withClassPath(input.getClassPath())
				.build();
		if(componentResourcesProvider.getResources().isEmpty()){
			result.setErrorMessage("There were no component resources found for component: " + input.getComponentName() + ". Ensure that your classpath is correct!");
		}
		return result;
	}

	@Override
	public int getOrder() {
		return ResolutionTest.HIGHEST_PRECEDENCE;
	}

}
