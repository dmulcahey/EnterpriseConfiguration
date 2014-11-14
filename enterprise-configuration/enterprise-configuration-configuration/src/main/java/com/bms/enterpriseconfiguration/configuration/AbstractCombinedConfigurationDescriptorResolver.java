package com.bms.enterpriseconfiguration.configuration;

import java.util.Set;

import com.bms.enterpriseconfiguration.core.AbstractResolver;
import com.bms.enterpriseconfiguration.resources.ResourceProvider;
import com.google.common.collect.Sets;

public abstract class AbstractCombinedConfigurationDescriptorResolver extends AbstractResolver<Set<ResourceProvider<?>>, Set<CombinedConfigurationDescriptor>> {

	@Override
	protected Set<CombinedConfigurationDescriptor> doResolution(Set<ResourceProvider<?>> input) {
		Set<CombinedConfigurationDescriptor> combinedConfigurationDescriptors = Sets.newHashSet();
		
		return combinedConfigurationDescriptors;
	}
	

}
