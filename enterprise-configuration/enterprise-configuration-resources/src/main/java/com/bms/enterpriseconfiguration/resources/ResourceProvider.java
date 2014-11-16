package com.bms.enterpriseconfiguration.resources;

import java.util.Set;

public interface ResourceProvider<T extends Set<? extends AbstractResource>> {

	T getResources();
	
	int getOrder();
	
	boolean isSecure();
	
}
