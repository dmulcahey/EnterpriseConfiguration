package com.bms.enterpriseconfiguration.resources.filter;

public class NameFilter implements ResourceFilter {

	private String resourceName;
	
	public NameFilter(String resourceName) {
		this.resourceName = resourceName;
	}

	@Override
	public boolean accept(String resourcePath) {
		return resourcePath.endsWith(resourceName);
	}

	@Override
	public String toString() {
		return "NameFilter [resourceName(" + resourceName + ")]";
	}
	
}
