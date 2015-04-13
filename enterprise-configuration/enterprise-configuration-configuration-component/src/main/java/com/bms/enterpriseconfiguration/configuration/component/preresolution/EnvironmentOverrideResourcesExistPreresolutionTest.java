package com.bms.enterpriseconfiguration.configuration.component.preresolution;

import com.bms.enterpriseconfiguration.configuration.component.ComponentConfigurationResolver.Criteria;
import com.bms.enterpriseconfiguration.configuration.component.annotation.ComponentConfigurationResolverPreresolutionTest;
import com.bms.enterpriseconfiguration.core.ResolutionTest;
import com.bms.enterpriseconfiguration.core.ResolutionTestResult;

@ComponentConfigurationResolverPreresolutionTest
public class EnvironmentOverrideResourcesExistPreresolutionTest implements ResolutionTest<Criteria> {

	@Override
	public ResolutionTestResult execute(Criteria input) {
		ResolutionTestResult result = new ResolutionTestResult();
		return result;
	}
	
	@Override
	public int getOrder() {
		return ResolutionTest.HIGHEST_PRECEDENCE;
	}

}
