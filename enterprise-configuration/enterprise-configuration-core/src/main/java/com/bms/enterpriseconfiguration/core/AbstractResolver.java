package com.bms.enterpriseconfiguration.core;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.SneakyThrows;

import org.reflections.Reflections;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
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
		String thisClassName = this.getClass().getSimpleName();
		logger.finest(thisClassName + " resolve start");
		CombinedResolutionTestResult preresolutionTestResults = new CombinedResolutionTestResult();
		int numPreresolutionTests = getPreresolutionTests().size();
		if(numPreresolutionTests > 0){
			logger.info("Executing " + numPreresolutionTests + " preresolution tests in " + thisClassName);
			List<ResolutionTest<I>> sortedPreresolutionTests = Lists.newArrayList(getPreresolutionTests());
			Collections.sort(sortedPreresolutionTests, Collections.reverseOrder(new ResolutionOrdering<ResolutionTest<I>>()));
			for(ResolutionTest<I> resolutionTest : sortedPreresolutionTests){
				String testClassName = resolutionTest.getClass().getSimpleName();
				logger.info("Executing preresolution test: " + testClassName);
				preresolutionTestResults.addResolutionTestResult(resolutionTest.execute(input), testClassName);
			}
			handlePreresolutionTestResults(preresolutionTestResults);
		}
		
		int numPreresolutionActivities = getPreresolutionActivities().size();
		if(numPreresolutionActivities > 0){
			logger.info("Executing " + numPreresolutionActivities + " preresolution activities in " + thisClassName);
			List<ResolutionActivity<I>> sortedPreresolutionActivities = Lists.newArrayList(getPreresolutionActivities());
			Collections.sort(sortedPreresolutionActivities, Collections.reverseOrder(new ResolutionOrdering<ResolutionActivity<I>>()));
			for(ResolutionActivity<I> resolutionActivity : sortedPreresolutionActivities){
				logger.info("Executing preresolution activity: " + resolutionActivity.getClass().getSimpleName());
				resolutionActivity.perform(input);
			}
		}
		
		logger.finest(thisClassName + " resolving...");
		O output = doResolution(input);
		
		CombinedResolutionTestResult postresolutionTestResults = new CombinedResolutionTestResult();
		int numPostresolutionTests = getPostresolutionTests().size();
		if(numPostresolutionTests > 0){
			logger.info("Executing " + numPostresolutionTests + " postresolution tests in " + thisClassName);
			List<ResolutionTest<O>> sortedPostresolutionTests = Lists.newArrayList(getPostresolutionTests());
			Collections.sort(sortedPostresolutionTests, Collections.reverseOrder(new ResolutionOrdering<ResolutionTest<O>>()));
			for(ResolutionTest<O> resolutionTest : sortedPostresolutionTests){
				String testClassName = resolutionTest.getClass().getSimpleName();
				logger.info("Executing postresolution test: " + testClassName);
				postresolutionTestResults.addResolutionTestResult(resolutionTest.execute(output), testClassName);
			}
			handlePostresolutionTestResults(postresolutionTestResults);
		}
		
		int numPostresolutionActivities = getPostresolutionActivities().size();
		if(numPostresolutionActivities > 0){
			logger.info("Executing " + numPostresolutionActivities + " postresolution activities in " + thisClassName);
			List<ResolutionActivity<O>> sortedPostresolutionActivities = Lists.newArrayList(getPostresolutionActivities());
			Collections.sort(sortedPostresolutionActivities, Collections.reverseOrder(new ResolutionOrdering<ResolutionActivity<O>>()));
			for(ResolutionActivity<O> postresolutionActivity : sortedPostresolutionActivities){
				logger.info("Executing postresolution activity: " + postresolutionActivity.getClass().getSimpleName());
				postresolutionActivity.perform(output);
			}
		}
		logger.finest(thisClassName + " resolve complete");
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
			for(ResolutionTestResult result : resolutionTestResult.getFailedTestResults()){
				if(result.getErrorMessage().isPresent()){
					logger.severe("Error in " + result.getTestClassName() + ": " + result.getErrorMessage().get());
				}
				if(result.getPossibleException().isPresent()){
					logger.log(Level.SEVERE, "Exception thrown in " + result.getTestClassName() , result.getPossibleException().get());
				}
			}
			throw new RuntimeException("Resolution Failed...");
		}else{
			for(ResolutionTestResult result : resolutionTestResult.getResolutionTestResults()){
				if(result.getInformationMessage().isPresent()){
					logger.info(result.getTestClassName() + ": " + result.getInformationMessage().get());
				}
				if(result.getWarningMessage().isPresent()){
					logger.warning(result.getTestClassName() + ": " + result.getWarningMessage().get());
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@SneakyThrows
	private void initialize(){
		String thisClassName = this.getClass().getSimpleName();
		logger.info("initializing " + thisClassName + " started");
		logger.fine("loading preresolution tests in " + thisClassName);
		Optional<Class<? extends Annotation>> preresolutionTestAnnotationClass = getPreresolutionTestAnnotationClass();
		if(preresolutionTestAnnotationClass.isPresent()){
			Set<Class<?>> preresolutionTests = REFLECTIONS.getTypesAnnotatedWith(preresolutionTestAnnotationClass.get());
			for(Class<?> preresolutionTestClass : preresolutionTests){
				this.addPreresolutionTest((ResolutionTest<I>) preresolutionTestClass.getConstructor().newInstance());
			}
		}
		logger.fine("loading postresolution tests in " + thisClassName);
		Optional<Class<? extends Annotation>> postresolutionTestAnnotationClass = getPostresolutionTestAnnotationClass();
		if(postresolutionTestAnnotationClass.isPresent()){
			Set<Class<?>> postresolutionTests = REFLECTIONS.getTypesAnnotatedWith(postresolutionTestAnnotationClass.get());
			for(Class<?> postresolutionTestClass : postresolutionTests){
				this.addPostresolutionTest((ResolutionTest<O>) postresolutionTestClass.getConstructor().newInstance());
			}
		}
		logger.fine("loading preresolution activities in " + thisClassName);
		Optional<Class<? extends Annotation>> preresolutionActivityAnnotationClass = getPreresolutionActivityAnnotationClass();
		if(preresolutionActivityAnnotationClass.isPresent()){
			Set<Class<?>> preresolutionActivities = REFLECTIONS.getTypesAnnotatedWith(preresolutionActivityAnnotationClass.get());
			for(Class<?> preresolutionActivityClass : preresolutionActivities){
				this.addPreresolutionActivity((ResolutionActivity<I>) preresolutionActivityClass.getConstructor().newInstance());
			}
		}
		logger.fine("loading postresolution activities in " + thisClassName);
		Optional<Class<? extends Annotation>> postresolutionActivityAnnotationClass = getPostresolutionActivityAnnotationClass();
		if(postresolutionActivityAnnotationClass.isPresent()){
			Set<Class<?>> postresolutionActivities = REFLECTIONS.getTypesAnnotatedWith(postresolutionActivityAnnotationClass.get());
			for(Class<?> postresolutionActivityClass : postresolutionActivities){
				this.addPostresolutionActivity((ResolutionActivity<O>) postresolutionActivityClass.getConstructor().newInstance());
			}
		}
		logger.info("initializing  " + thisClassName + " complete");
	}
	
}
