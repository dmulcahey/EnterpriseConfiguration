package com.bms.enterpriseconfiguration.resources;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

public class ResourceLocatorProviderOrdering extends Ordering<ResourceLocatorProvider> {

	@Override
	public int compare(ResourceLocatorProvider left, ResourceLocatorProvider right) {
		return Ints.compare(left.getOrder(), right.getOrder());
	}

}
