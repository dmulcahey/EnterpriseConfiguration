package com.bms.enterpriseconfiguration.core;

public interface ResolutionActivity<T> {

	void perform(T input);
	
}
