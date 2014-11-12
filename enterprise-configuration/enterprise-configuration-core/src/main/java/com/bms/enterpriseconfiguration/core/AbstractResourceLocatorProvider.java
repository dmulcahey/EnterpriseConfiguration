package com.bms.enterpriseconfiguration.core;

public abstract class AbstractResourceLocatorProvider implements ResourceLocatorProvider {

	@Override
	public boolean isRequired() {
		return true;
	}

	@Override
	public String toString() {
		return "AbstractResourceLocatorProvider [isRequired()=" + isRequired() + ", getResourceLocator=" + getResourceLocator() + ", getOrder=" + getOrder() + "]";
	}
	
}
