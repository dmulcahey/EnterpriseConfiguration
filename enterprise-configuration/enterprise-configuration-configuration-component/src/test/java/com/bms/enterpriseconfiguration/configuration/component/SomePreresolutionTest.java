package com.bms.enterpriseconfiguration.configuration.component;

import java.util.logging.Logger;

import com.bms.enterpriseconfiguration.configuration.component.ComponentConfigurationResolver.Criteria;
import com.bms.enterpriseconfiguration.configuration.component.annotation.ComponentConfigurationResolverPreresolutionTest;
import com.bms.enterpriseconfiguration.core.ResolutionTest;
import com.bms.enterpriseconfiguration.core.ResolutionTestResult;

@ComponentConfigurationResolverPreresolutionTest
public class SomePreresolutionTest implements ResolutionTest<Criteria>{

	@Override
	public ResolutionTestResult execute(Criteria input) {
		Logger.getAnonymousLogger().info("Here are your criteria!!! : " + input);
		ResolutionTestResult result = new ResolutionTestResult();
		result.setInformationMessage("YO Yo Yo Yo Yo!");
		return result;
	}

}
