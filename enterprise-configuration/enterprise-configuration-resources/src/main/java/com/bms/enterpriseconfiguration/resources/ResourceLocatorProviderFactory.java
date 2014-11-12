package com.bms.enterpriseconfiguration.resources;

import java.util.Collections;
import java.util.Set;

import com.bms.enterpriseconfiguration.resources.filter.PathFilter;
import com.bms.enterpriseconfiguration.resources.filter.ResourceFilter;
import com.google.common.collect.Sets;

public class ResourceLocatorProviderFactory {

	public static ResourceLocatorProvider create(final String name, final int order, final boolean required, String... pathFilters){
		return ResourceLocatorProviderFactory.create(name, order, required, new PathFilter(pathFilters));
	}
	
	public static ResourceLocatorProvider create(final String name, final int order, final boolean required, final ResourceFilter... resourceFilters){
		
		final Set<ResourceFilter> filters = Sets.newHashSet();
		Collections.addAll(filters, resourceFilters);
		
		AbstractResourceLocatorProvider provider = new AbstractResourceLocatorProvider() {
			@Override
			public Set<ResourceFilter> getFilters() {
				return filters;
			}
			
			@Override
			public boolean isRequired() {
				return required;
			}

			@Override
			public int getOrder() {
				return order;
			}
			
		};
		provider.setName(name);
		return provider;
	}
	
	public static ResourceLocatorProvider create(final String name, final int order){
		return ResourceLocatorProviderFactory.create(name, order, true, name);
	}
	
	public static ResourceLocatorProvider createFrom(ResourceLocatorProvider resourceLocatorProvider, String name, String... paths){
		return ResourceLocatorProviderFactory.create(name, resourceLocatorProvider.getOrder(), resourceLocatorProvider.isRequired(), paths);
	}
	
	public static ResourceLocatorProvider createFrom(ResourceLocatorProvider resourceLocatorProvider, String name){
		return ResourceLocatorProviderFactory.createFrom(resourceLocatorProvider, name, name);
	}
	
}
