package com.bms.enterpriseconfiguration.resources;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

public class ResourceProviderOrdering extends Ordering<ResourceProvider<?>> {

	@Override
	public int compare(ResourceProvider<?> left, ResourceProvider<?> right) {
		return Ints.compare(left.getOrder(), right.getOrder());
	}

}
