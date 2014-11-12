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
	
	public boolean isSuccessful(){
		boolean successful = true;
		for(ResolutionTestResult resolutionTestResult : getResolutionTestResults()){
			successful = successful && resolutionTestResult.isSuccessful();
		}
		return successful;
	}
	
}
