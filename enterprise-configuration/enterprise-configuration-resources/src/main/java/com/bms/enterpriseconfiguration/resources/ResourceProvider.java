package com.bms.enterpriseconfiguration.resources;

public interface ResourceProvider<T> {

	T getResources();
	
	int getOrder();
	
}
