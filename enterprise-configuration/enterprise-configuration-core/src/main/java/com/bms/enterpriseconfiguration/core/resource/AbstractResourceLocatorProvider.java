package com.bms.enterpriseconfiguration.core.resource;

import com.google.common.base.Optional;

public abstract class AbstractResourceLocatorProvider implements ResourceLocatorProvider {

	@Override
	public boolean isRequired() {
		return true;
	}
	
	@Override
	public Optional<String> getExcludesLocator() {
		return Optional.absent();
	}

	@Override
	public String toString() {
		return "AbstractResourceLocatorProvider [isRequired()=" + isRequired() + ", getResourceLocator=" + getResourceLocator() + ", getOrder=" + getOrder() + ", getExcludesLocator=" + getExcludesLocator() + "]";
	}
	
}
