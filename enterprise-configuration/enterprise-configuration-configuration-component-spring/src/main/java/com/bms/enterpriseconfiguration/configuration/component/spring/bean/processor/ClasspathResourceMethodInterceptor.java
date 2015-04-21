package com.bms.enterpriseconfiguration.configuration.component.spring.bean.processor;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.bms.enterpriseconfiguration.configuration.component.ComponentConfigurationManager;

public class ClasspathResourceMethodInterceptor implements MethodInterceptor {
	
	private String resourceName;
	private String componentName;
	private String environmentName;

	public ClasspathResourceMethodInterceptor(String componentName, String resourceName, String environmentName) {
		super();
		this.resourceName = resourceName;
		this.componentName = componentName;
		this.environmentName = environmentName;
	}

	@Override
	public Object intercept(Object target, Method method, Object[] arguments, MethodProxy methodProxy) throws Throwable {
		// this check prevents the real injected resource from being overwritten by the placeholder resource
		if(!method.getName().equalsIgnoreCase("setResource")){
			return methodProxy.invoke(ComponentConfigurationManager.getComponentConfiguration(componentName, environmentName).getResource(resourceName), arguments);
		}else{
			return null;
		}
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
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
