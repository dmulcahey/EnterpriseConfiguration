package com.bms.enterpriseconfiguration.core;

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
	
}
