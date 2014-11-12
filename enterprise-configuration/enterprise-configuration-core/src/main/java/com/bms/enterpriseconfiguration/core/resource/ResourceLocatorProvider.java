package com.bms.enterpriseconfiguration.core.resource;

import com.google.common.base.Optional;

public interface ResourceLocatorProvider {

	String getResourceLocator();
	
	boolean isRequired();
	
	int getOrder();
	
	Optional<String> getExcludesLocator();
	
}
