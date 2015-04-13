package com.bms.enterpriseconfiguration.core;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class CombinedResolutionTestResult {

	private List<ResolutionTestResult> resolutionTestResults;

	public List<ResolutionTestResult> getResolutionTestResults() {
		if(resolutionTestResults == null){
			resolutionTestResults = Lists.newArrayList();
		}
		return resolutionTestResults;
	}
	
	public boolean addResolutionTestResult(ResolutionTestResult resolutionTestResult, String testClassName){
		resolutionTestResult.setTestClassName(testClassName);
		return this.getResolutionTestResults().add(resolutionTestResult);
	}
	
	public boolean isSuccessful(){
		boolean successful = true;
		for(ResolutionTestResult resolutionTestResult : getResolutionTestResults()){
			successful = successful && resolutionTestResult.isSuccessful();
		}
		return successful;
	}
	
	public List<ResolutionTestResult> getFailedTestResults(){
		return Lists.newArrayList(Iterables.filter(getResolutionTestResults(), 
				new Predicate<ResolutionTestResult>(){
					@Override
					public boolean apply(ResolutionTestResult input) {
						return !input.isSuccessful();
					}
				}));
	}
	
}
