package com.bms.enterpriseconfiguration.core;

import java.util.List;

import com.google.common.collect.Lists;

public class CombinedResolutionTestResult {

	private List<ResolutionTestResult> resolutionTestResults;

	public List<ResolutionTestResult> getResolutionTestResults() {
		if(resolutionTestResults == null){
			resolutionTestResults = Lists.newArrayList();
		}
		return resolutionTestResults;
	}
	
	
}
