package com.bms.enterpriseconfiguration.core;

import java.util.Collections;
import java.util.List;

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
	
	protected List<ResolutionActivity<INPUT>> getPreresolutionResolutionActivities(){
		return Collections.emptyList();
	}
	
	protected List<ResolutionActivity<OUTPUT>> getPostresolutionResolutionActivities(){
		return Collections.emptyList();
	}
	
	protected List<ResolutionTest<INPUT>> getPreresolutionResolutionTests(){
		return Collections.emptyList();
	}
	
	protected List<ResolutionTest<OUTPUT>> getPostresolutionResolutionTests(){
		return Collections.emptyList();
	}

}
