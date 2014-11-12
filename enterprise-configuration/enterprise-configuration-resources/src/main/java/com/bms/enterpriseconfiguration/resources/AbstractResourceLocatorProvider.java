package com.bms.enterpriseconfiguration.resources;


public abstract class AbstractResourceLocatorProvider implements ResourceLocatorProvider {

	private String name;
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

	@Override
	public String toString() {
		return "AbstractResourceLocatorProvider [name(" + name + "), isRequired(" + isRequired() + "), getFilters(" + getFilters() + "), getOrder(" + getOrder() + ")]";
	}
	
}
