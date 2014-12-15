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
public abstract class AbstractResolver<I,O> implements Resolver<I,O> {
	
	private Set<ResolutionActivity<I>> preresolutionActivities;
	private Set<ResolutionActivity<O>> postresolutionActivities;
	private Set<ResolutionTest<I>> preresolutionTests;
	private Set<ResolutionTest<O>> postresolutionTests;

	protected abstract O doResolution(final I input);
	
	@Override
	public final O resolve(final I input) {
		
		CombinedResolutionTestResult preresolutionTestResults = new CombinedResolutionTestResult();
		for(ResolutionTest<I> resolutionTest : getPreresolutionTests()){
			preresolutionTestResults.addResolutionTestResult(resolutionTest.execute(input));
		}
		handlePreresolutionTestResults(preresolutionTestResults);
		
		for(ResolutionActivity<I> resolutionActivity : getPreresolutionActivities()){
			resolutionActivity.perform(input);
		}
		
		O output = doResolution(input);
		
		CombinedResolutionTestResult postresolutionTestResults = new CombinedResolutionTestResult();
		for(ResolutionTest<O> resolutionTest : getPostresolutionTests()){
			postresolutionTestResults.addResolutionTestResult(resolutionTest.execute(output));
		}
		handlePostresolutionTestResults(postresolutionTestResults);
		
		for(ResolutionActivity<O> postresolutionActivity : getPostresolutionActivities()){
			postresolutionActivity.perform(output);
		}
		
		return output;
	}
	
	public final Set<ResolutionActivity<I>> getPreresolutionActivities(){
		if(preresolutionActivities == null){
			preresolutionActivities = Sets.newHashSet();
		}
		return preresolutionActivities;
	}
	
	public final Set<ResolutionActivity<O>> getPostresolutionActivities(){
		if(postresolutionActivities == null){
			postresolutionActivities = Sets.newHashSet();
		}
		return postresolutionActivities;
	}
	
	public final Set<ResolutionTest<I>> getPreresolutionTests(){
		if(preresolutionTests == null){
			preresolutionTests = Sets.newHashSet();
		}
		return preresolutionTests;
	}
	
	public final Set<ResolutionTest<O>> getPostresolutionTests(){
		if(postresolutionTests == null){
			postresolutionTests = Sets.newHashSet();
		}
		return postresolutionTests;
	}
	
	public final AbstractResolver<I,O> addPreresolutionActivity(final ResolutionActivity<I> preresolutionActivity){
		this.getPreresolutionActivities().add(preresolutionActivity);
		return this;
	}
	
	public final AbstractResolver<I,O> addPostresolutionActivity(final ResolutionActivity<O> postresolutionActivity){
		this.getPostresolutionActivities().add(postresolutionActivity);
		return this;
	}
	
	public final AbstractResolver<I,O> addPreresolutionTest(final ResolutionTest<I> preresolutionTest){
		this.getPreresolutionTests().add(preresolutionTest);
		return this;
	}
	
	public final AbstractResolver<I,O> addPostresolutionTest(final ResolutionTest<O> postresolutionTest){
		this.getPostresolutionTests().add(postresolutionTest);
		return this;
	}
	
}
