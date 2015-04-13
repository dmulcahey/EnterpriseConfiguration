package com.bms.enterpriseconfiguration.core;

public interface ResolutionActivity<T> extends Ordered{

	void perform(T input);
	
}
