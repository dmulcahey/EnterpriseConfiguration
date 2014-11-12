package com.bms.enterpriseconfiguration.resources;

import java.util.Set;

import com.bms.enterpriseconfiguration.resources.filter.ResourceFilter;

public interface ResourceLocatorProvider {

	String getName();
	
	Set<ResourceFilter> getFilters();
	
	boolean isRequired();
	
	int getOrder();
	
}
