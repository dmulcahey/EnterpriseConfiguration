package com.bms.enterpriseconfiguration.configuration.component.preresolution;

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
public class SharedResourcesExistPreresolutionTest implements ResolutionTest<Criteria> {

	@Override
	public ResolutionTestResult execute(Criteria input) {
		ResolutionTestResult result = new ResolutionTestResult();
		OrFilter extensionFilter = new OrFilter(ExtensionFilter.PROPERTIES_FILTER, ExtensionFilter.XML_FILTER);
		FilteredClasspathResourceResourceProvider sharedResourcesProvider = FilteredClasspathResourceResourceProvider.builder()
				.order(100)
				.withResourceFilter(new PathFilter(ComponentConfigurationResolver.SHARED_RESOURCES_LOCATOR))
				.withResourceFilter(extensionFilter)
				.withClassPath(input.getClassPath())
				.build();
		if(sharedResourcesProvider.getResources().isEmpty()){
			result.setErrorMessage("There were no shared resources detected. Ensure that the classpath is correct!");
		}
		return result;
	}
	
	@Override
	public int getOrder() {
		return ResolutionTest.HIGHEST_PRECEDENCE;
	}

}
