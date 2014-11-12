package com.bms.enterpriseconfiguration.core;

public interface ResolutionTest<T> {

	ResolutionTestResult execute(T input);
	
}
