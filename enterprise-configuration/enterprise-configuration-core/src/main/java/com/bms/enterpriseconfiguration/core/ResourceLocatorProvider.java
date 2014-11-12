package com.bms.enterpriseconfiguration.core;

public interface ResourceLocatorProvider {

	String getResourceLocator();
	
	boolean isRequired();
	
	int getOrder();
	
}
