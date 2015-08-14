package com.bms.enterpriseconfiguration.configuration.component;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;

import org.reflections.Reflections;

@CommonsLog
public class ComponentConfigurationManagerServletContainerInitializer implements ServletContainerInitializer {

	@Override
	@SneakyThrows
	public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
		log.info("Beginning configuration initialization...");
		log.info("Loading : " + ComponentConfigurationManager.class.getName() + " at servlet container startup...");
		ComponentConfigurationManager.noOp();
		log.info("Searching for ComponentConfigurationInitializers...");
		Reflections reflections = new Reflections();
		Set<Class<? extends ComponentConfigurationInitializer>> componentConfigurationInitializers = reflections.getSubTypesOf(ComponentConfigurationInitializer.class);
		for(Class<? extends ComponentConfigurationInitializer> componentConfigurationInitializerImplementation : componentConfigurationInitializers){
			log.info("Executing ComponentConfigurationInitializer: " + componentConfigurationInitializerImplementation.getName());
			componentConfigurationInitializerImplementation.newInstance().onStartup();
		}
		log.info("Configuration initialization complete.");
	}

}
