package com.bms.enterpriseconfiguration.core;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.logging.Logger;
import lombok.SneakyThrows;

import org.reflections.Reflections;

import com.google.common.base.Optional;
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
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private static final Reflections REFLECTIONS = new Reflections();
	private Set<ResolutionActivity<I>> preresolutionActivities;
	private Set<ResolutionActivity<O>> postresolutionActivities;
	private Set<ResolutionTest<I>> preresolutionTests;
	private Set<ResolutionTest<O>> postresolutionTests;
	
	public AbstractResolver(){
		initialize();
	}

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
	
	public void handlePreresolutionTestResults(CombinedResolutionTestResult preresolutionTestResult){
		handleResolutionTestResults(preresolutionTestResult);
	}
	
	public void handlePostresolutionTestResults(CombinedResolutionTestResult postresolutionTestResult){
		handleResolutionTestResults(postresolutionTestResult);
	}
	
	public Optional<Class<? extends Annotation>> getPreresolutionTestAnnotationClass(){
		return Optional.absent();
	}
	
	public Optional<Class<? extends Annotation>> getPostresolutionTestAnnotationClass(){
		return Optional.absent();
	}
	
	public Optional<Class<? extends Annotation>> getPreresolutionActivityAnnotationClass(){
		return Optional.absent();
	}
	
	public Optional<Class<? extends Annotation>> getPostresolutionActivityAnnotationClass(){
		return Optional.absent();
	}
	
	private void handleResolutionTestResults(CombinedResolutionTestResult resolutionTestResult){
		if(!resolutionTestResult.isSuccessful()){
			throw new RuntimeException("Resolution Failed...");
		}
	}
	
	@SuppressWarnings("unchecked")
	@SneakyThrows
	private void initialize(){
		logger.info("initializing started...");
		logger.fine("loading preresolution tests");
		Optional<Class<? extends Annotation>> preresolutionTestAnnotationClass = getPreresolutionTestAnnotationClass();
		if(preresolutionTestAnnotationClass.isPresent()){
			Set<Class<?>> preresolutionTests = REFLECTIONS.getTypesAnnotatedWith(preresolutionTestAnnotationClass.get());
			for(Class<?> preresolutionTestClass : preresolutionTests){
				this.addPreresolutionTest((ResolutionTest<I>) preresolutionTestClass.getConstructor().newInstance());
			}
		}
		logger.fine("loading postresolution tests");
		Optional<Class<? extends Annotation>> postresolutionTestAnnotationClass = getPostresolutionTestAnnotationClass();
		if(postresolutionTestAnnotationClass.isPresent()){
			Set<Class<?>> postresolutionTests = REFLECTIONS.getTypesAnnotatedWith(postresolutionTestAnnotationClass.get());
			for(Class<?> postresolutionTestClass : postresolutionTests){
				this.addPostresolutionTest((ResolutionTest<O>) postresolutionTestClass.getConstructor().newInstance());
			}
		}
		logger.fine("loading preresolution activities");
		Optional<Class<? extends Annotation>> preresolutionActivityAnnotationClass = getPreresolutionActivityAnnotationClass();
		if(preresolutionActivityAnnotationClass.isPresent()){
			Set<Class<?>> preresolutionActivitiess = REFLECTIONS.getTypesAnnotatedWith(preresolutionActivityAnnotationClass.get());
			for(Class<?> preresolutionActivityClass : preresolutionActivitiess){
				this.addPreresolutionActivity((ResolutionActivity<I>) preresolutionActivityClass.getConstructor().newInstance());
			}
		}
		logger.fine("loading postresolution activities");
		Optional<Class<? extends Annotation>> postresolutionActivityAnnotationClass = getPostresolutionActivityAnnotationClass();
		if(postresolutionActivityAnnotationClass.isPresent()){
			Set<Class<?>> postresolutionActivitiess = REFLECTIONS.getTypesAnnotatedWith(postresolutionActivityAnnotationClass.get());
			for(Class<?> postresolutionActivityClass : postresolutionActivitiess){
				this.addPostresolutionActivity((ResolutionActivity<O>) postresolutionActivityClass.getConstructor().newInstance());
			}
		}
		logger.info("initializing complete...");
	}
	
}
