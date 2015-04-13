package com.bms.enterpriseconfiguration.core;

public interface ResolutionTest<T> extends Ordered{
	
	ResolutionTestResult execute(T input);
	
}
