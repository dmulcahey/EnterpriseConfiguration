package com.bms.enterpriseconfiguration.resources.classpath.filter;

public class ExtensionFilter extends NameFilter {

	public ExtensionFilter(String resourceName) {
		super(resourceName);
	}

	@Override
	public String toString() {
		return "ExtensionFilter [toString(" + super.toString() + ")]";
	}

}