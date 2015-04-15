package com.bms.enterpriseconfiguration.configuration.classpath;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Method;
import java.util.Map;

import com.bms.enterpriseconfiguration.configuration.component.ComponentConfiguration;
import com.google.common.reflect.AbstractInvocationHandler;

public class CombinedClasspathConfigurationProxy extends AbstractInvocationHandler {
	
	private final String componentName;
	private final String environment;
	private final String configurationName;
	private final Map<String, Map<String, ComponentConfiguration>> componentConfigurationsByEnvironment;

	@Override
	protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
		Map<String, ComponentConfiguration> componentConfigurationsInEnvironment = this.componentConfigurationsByEnvironment.get(this.environment);
		ComponentConfiguration componentConfiguration = componentConfigurationsInEnvironment.get(this.componentName);
		return method.invoke(componentConfiguration.getConfiguration(this.configurationName), args);
	}
	
	@Override
	public String toString() {
		Map<String, ComponentConfiguration> componentConfigurationsInEnvironment = this.componentConfigurationsByEnvironment.get(this.environment);
		ComponentConfiguration componentConfiguration = componentConfigurationsInEnvironment.get(this.componentName);
		return componentConfiguration.getConfiguration(this.configurationName).toString();
	}
	
	public static Builder builder(){
		return new Builder();
	}
	
	public static class Builder {
		
		private String componentName;
		private String environment;
		private String configurationName;
		private Map<String, Map<String, ComponentConfiguration>> componentConfigurationsByEnvironment;
		
		private Builder(){}
		
		public Builder componentName(String componentName){
			this.componentName = checkNotNull(componentName, "The componentName can not be null.");
			return this;
		}
		
		public Builder environment(String environment){
			this.environment = checkNotNull(environment, "The environment can not be null.");
			return this;
		}
		
		public Builder configurationName(String configurationName){
			this.configurationName = checkNotNull(configurationName, "The configurationName can not be null.");
			return this;
		}
		
		//live reference to an existing map. this map MUST be thread safe!!!
		public Builder componentConfigurationsByEnvironment(Map<String, Map<String, ComponentConfiguration>> componentConfigurationsByEnvironment){
			this.componentConfigurationsByEnvironment = checkNotNull(componentConfigurationsByEnvironment, "The componentConfigurationsByEnvironment can not be null.");
			return this;
		}
		
		public CombinedClasspathConfigurationProxy build(){
			return new CombinedClasspathConfigurationProxy(this);
		}
		
	}
	
	private CombinedClasspathConfigurationProxy(Builder builder){
		this.componentName = builder.componentName;
		this.configurationName = builder.configurationName;
		this.environment = builder.environment;
		this.componentConfigurationsByEnvironment = builder.componentConfigurationsByEnvironment;
	}

}
