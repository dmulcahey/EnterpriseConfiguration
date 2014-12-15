package com.bms.enterpriseconfiguration.core;

import java.util.Set;

public interface Resolver<I,O> {

	O resolve(I input);
	
	Set<ResolutionActivity<I>> getPreresolutionActivities();
	
	Set<ResolutionActivity<O>> getPostresolutionActivities();
	
	Set<ResolutionTest<I>> getPreresolutionTests();
	
	Set<ResolutionTest<O>> getPostresolutionTests();
	
	void handlePreresolutionTestResults(CombinedResolutionTestResult preresolutionTestResult);
	
	void handlePostresolutionTestResults(CombinedResolutionTestResult postresolutionTestResult);
	
}
