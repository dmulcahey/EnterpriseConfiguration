package com.bms.enterpriseconfiguration.resources;

import com.google.common.base.Optional;

public class ResourceLocatorProviderFactory {

	public static ResourceLocatorProvider create(final String resourceLocator, final int order){
		return new AbstractResourceLocatorProvider() {
			@Override
			public String getResourceLocator() {
				return resourceLocator;
			}

			@Override
			public int getOrder() {
				return order;
			}
		};
	}
	
	public static ResourceLocatorProvider create(final String resourceLocator, final int order, final boolean required){
		return new AbstractResourceLocatorProvider() {
			@Override
			public String getResourceLocator() {
				return resourceLocator;
			}

			@Override
			public int getOrder() {
				return order;
			}

			@Override
			public boolean isRequired() {
				return required;
			}
		};
	}
	
	public static ResourceLocatorProvider create(final String resourceLocator, final int order, final boolean required, final String excludesLocator){
		return new AbstractResourceLocatorProvider() {
			@Override
			public String getResourceLocator() {
				return resourceLocator;
			}

			@Override
			public int getOrder() {
				return order;
			}

			@Override
			public boolean isRequired() {
				return required;
			}

			@Override
			public Optional<String> getExcludesLocator() {
				return Optional.fromNullable(excludesLocator);
			}
		};
	}
	
	public static ResourceLocatorProvider createFrom(final String resourceLocator, ResourceLocatorProvider resourceLocatorProvider){
		return ResourceLocatorProviderFactory.create(resourceLocator, resourceLocatorProvider.getOrder(), resourceLocatorProvider.isRequired(), resourceLocatorProvider.getExcludesLocator().orNull());
	}
	
	public static ResourceLocatorProvider createCombined(final String resourceLocator, ResourceLocatorProvider resourceLocatorProvider){
		return ResourceLocatorProviderFactory.createFrom(resourceLocatorProvider.getResourceLocator() + "/" + resourceLocator, resourceLocatorProvider);
	}
	
}
