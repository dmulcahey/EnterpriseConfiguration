package com.bms.enterpriseconfiguration.resources;

import com.google.common.collect.Ordering;

public class ResourceOrdering extends Ordering<AbstractResource>{

	private static final ResourceProviderOrdering RESOURCE_PROVIDER_ORDERING = new ResourceProviderOrdering();
	
	@Override
	public int compare(AbstractResource left, AbstractResource right) {
		return RESOURCE_PROVIDER_ORDERING.compare(left.getResourceProvider(), right.getResourceProvider());
	}

}
