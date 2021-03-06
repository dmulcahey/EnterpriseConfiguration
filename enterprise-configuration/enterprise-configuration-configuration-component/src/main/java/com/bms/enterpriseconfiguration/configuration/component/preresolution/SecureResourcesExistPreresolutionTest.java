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
import com.bms.enterpriseconfiguration.resources.classpath.filter.OrFilter;
import com.bms.enterpriseconfiguration.resources.classpath.filter.PathFilter;

@ComponentConfigurationResolverPreresolutionTest
public class SecureResourcesExistPreresolutionTest implements ResolutionTest<Criteria> {

	@Override
	public ResolutionTestResult execute(Criteria input) {
		ResolutionTestResult result = new ResolutionTestResult();
		Map<String,String> variables = ComponentConfigurationResolver.buildVariables(input);
		OrFilter extensionFilter = new OrFilter(ExtensionFilter.PROPERTIES_FILTER, ExtensionFilter.XML_FILTER);
		FilteredClasspathResourceResourceProvider secureResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
				.order(500)
				.secure(true)
				.withResourceFilter(new PathFilter(StrSubstitutor.replace(ComponentConfigurationResolver.SECURE_RESOURCES_LOCATOR_TEMPLATE, variables)))
				.withResourceFilter(extensionFilter)
				.withClassPath(input.getClassPath())
				.build();
		if(secureResourcesProvider.getResources().isEmpty()){
			result.setWarningMessage("There were no secure resources detected for component: " + input.getComponentName() + " in environment: " + input.getEnvironment());
		}
		return result;
	}
	
	@Override
	public int getOrder() {
		return ResolutionTest.HIGHEST_PRECEDENCE;
	}

}
