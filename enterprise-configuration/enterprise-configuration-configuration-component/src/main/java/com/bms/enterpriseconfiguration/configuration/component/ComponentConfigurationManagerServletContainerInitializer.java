package com.bms.enterpriseconfiguration.configuration.component;

import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ComponentConfigurationManagerServletContainerInitializer implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
		Logger.getLogger(getClass().getName()).info("Loading : " + ComponentConfigurationManager.class.getName() + " at servlet container startup...");
		ComponentConfigurationManager.noOp();
	}

}
