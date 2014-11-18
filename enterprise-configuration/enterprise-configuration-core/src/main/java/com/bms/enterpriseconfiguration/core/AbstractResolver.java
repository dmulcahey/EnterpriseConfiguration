package com.bms.enterpriseconfiguration.core;

import java.util.Set;

import com.google.common.collect.Sets;

/*
 * Flow: 
 * 
 * Execute preresolution tests on input
 * Perform preresolution activities using valid input
 * Invoke doResolution if all preresolution tests pass
 * Execute postresolution tests passing output from doResolution
 * Perform postresolution activities using valid output
 * 
 */
public abstract class AbstractResolver<INPUT,OUTPUT> implements Resolver<INPUT,OUTPUT> {
	
	private Set<ResolutionActivity<INPUT>> preresolutionResolutionActivities;
	private Set<ResolutionActivity<OUTPUT>> postresolutionResolutionActivities;
	private Set<ResolutionTest<INPUT>> preresolutionResolutionTests;
	private Set<ResolutionTest<OUTPUT>> postresolutionResolutionTests;

	protected abstract OUTPUT doResolution(INPUT input);
	
	@Override
	public OUTPUT resolve(INPUT input) {
		
		CombinedResolutionTestResult preresolutionResolutionTestResults = new CombinedResolutionTestResult();
		for(ResolutionTest<INPUT> resolutionTest : getPreresolutionResolutionTests()){
			preresolutionResolutionTestResults.getResolutionTestResults().add(resolutionTest.execute(input));
		}
		
		for(ResolutionActivity<INPUT> preresolutionResolutionActivity : getPreresolutionResolutionActivities()){
			preresolutionResolutionActivity.perform(input);
		}
		
		OUTPUT output = doResolution(input);
		
		CombinedResolutionTestResult postresolutionResolutionTestResults = new CombinedResolutionTestResult();
		for(ResolutionTest<OUTPUT> resolutionTest : getPostresolutionResolutionTests()){
			postresolutionResolutionTestResults.getResolutionTestResults().add(resolutionTest.execute(output));
		}
		
		for(ResolutionActivity<OUTPUT> postresolutionResolutionActivity : getPostresolutionResolutionActivities()){
			postresolutionResolutionActivity.perform(output);
		}
		
		return output;
	}
	
	protected final Set<ResolutionActivity<INPUT>> getPreresolutionResolutionActivities(){
		if(preresolutionResolutionActivities == null){
			preresolutionResolutionActivities = Sets.newHashSet();
		}
		return preresolutionResolutionActivities;
	}
	
	protected final Set<ResolutionActivity<OUTPUT>> getPostresolutionResolutionActivities(){
		if(postresolutionResolutionActivities == null){
			postresolutionResolutionActivities = Sets.newHashSet();
		}
		return postresolutionResolutionActivities;
	}
	
	protected final Set<ResolutionTest<INPUT>> getPreresolutionResolutionTests(){
		if(preresolutionResolutionTests == null){
			preresolutionResolutionTests = Sets.newHashSet();
		}
		return preresolutionResolutionTests;
	}
	
	protected final Set<ResolutionTest<OUTPUT>> getPostresolutionResolutionTests(){
		if(postresolutionResolutionTests == null){
			postresolutionResolutionTests = Sets.newHashSet();
		}
		return postresolutionResolutionTests;
	}
	
}
