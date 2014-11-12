package com.bms.enterpriseconfiguration.resources.filter;

import static com.google.common.base.Preconditions.checkArgument;

public class NotFilter implements ResourceFilter {
	
	private ResourceFilter resourceFilter;

	public NotFilter(ResourceFilter resourceFilter) {
		checkArgument(resourceFilter != null, "The supplied ResourceFilter must not be null!");
		this.resourceFilter = resourceFilter;
	}

	@Override
	public boolean accept(String resourcePath) {
		return !resourceFilter.accept(resourcePath);
	}

	@Override
	public String toString() {
		return "NotFilter [resourceFilter(" + resourceFilter + ")]";
	}

}
