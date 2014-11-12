package com.bms.enterpriseconfiguration.resources.filter;

public interface ResourceFilter {

	boolean accept(String resourcePath);
	
}
