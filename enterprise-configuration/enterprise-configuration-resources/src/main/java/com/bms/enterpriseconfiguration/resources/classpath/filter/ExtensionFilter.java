package com.bms.enterpriseconfiguration.resources.classpath.filter;

public class ExtensionFilter extends NameFilter {

	public static final ExtensionFilter PROPERTIES_FILTER = new ExtensionFilter(".properties");
	public static final ExtensionFilter XML_FILTER = new ExtensionFilter(".xml");
	public static final ExtensionFilter CLASS_FILTER = new ExtensionFilter(".class");
	
	public ExtensionFilter(String resourceName) {
		super(resourceName);
	}

	@Override
	public String toString() {
		return "ExtensionFilter [toString(" + super.toString() + ")]";
	}

}
