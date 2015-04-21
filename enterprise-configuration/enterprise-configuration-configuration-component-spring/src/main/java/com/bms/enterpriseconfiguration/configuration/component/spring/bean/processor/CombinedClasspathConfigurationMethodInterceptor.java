package com.bms.enterpriseconfiguration.configuration.component.spring.bean.processor;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.bms.enterpriseconfiguration.configuration.component.ComponentConfigurationManager;

public class CombinedClasspathConfigurationMethodInterceptor implements MethodInterceptor {

	private String configurationName;
	private String componentName;
	private String environmentName;
	
	public CombinedClasspathConfigurationMethodInterceptor(String componentName, String configurationName, String environmentName) {
		super();
		this.configurationName = configurationName;
		this.componentName = componentName;
		this.environmentName = environmentName;
	}

	@Override
	public Object intercept(Object target, Method method, Object[] arguments, MethodProxy methodProxy) throws Throwable {
		return methodProxy.invoke(ComponentConfigurationManager.getComponentConfiguration(componentName, environmentName).getConfiguration(configurationName), arguments);
	}

	public String getConfigurationName() {
		return configurationName;
	}

	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	
}
