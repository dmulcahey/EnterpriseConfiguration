package com.bms.enterpriseconfiguration.configuration.classpath;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Method;

import com.bms.enterpriseconfiguration.configuration.component.ComponentConfiguration;
import com.bms.enterpriseconfiguration.configuration.component.ComponentConfigurationResolver;
import com.google.common.reflect.AbstractInvocationHandler;

public class CombinedClasspathConfigurationProxy extends AbstractInvocationHandler {
	
	private String componentName;
	private String environment;
	private String configurationName;
	private ComponentConfigurationResolver componentConfigurationResolver;

	@Override
	protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
		ComponentConfigurationResolver.Criteria criteria = new ComponentConfigurationResolver.Criteria();
		criteria.setComponentName(this.componentName);
		criteria.setEnvironment(this.environment);
		ComponentConfiguration componentConfiguration = this.componentConfigurationResolver.resolve(criteria);
		return method.invoke(componentConfiguration.getConfiguration(this.configurationName), args);
	}
	
	@Override
	public String toString() {
		ComponentConfigurationResolver.Criteria criteria = new ComponentConfigurationResolver.Criteria();
		criteria.setComponentName(this.componentName);
		criteria.setEnvironment(this.environment);
		ComponentConfiguration componentConfiguration = this.componentConfigurationResolver.resolve(criteria);
		return componentConfiguration.getConfiguration(this.configurationName).toString();
	}
	
	public static Builder builder(){
		return new Builder();
	}
	
	public static class Builder {
		
		private String componentName;
		private String environment;
		private String configurationName;
		private ComponentConfigurationResolver componentConfigurationResolver;
		
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
		
		public Builder componentConfigurationResolver(ComponentConfigurationResolver componentConfigurationResolver){
			this.componentConfigurationResolver = checkNotNull(componentConfigurationResolver, "The componentConfigurationResolver can not be null.");
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
		this.componentConfigurationResolver = builder.componentConfigurationResolver;
	}

}
