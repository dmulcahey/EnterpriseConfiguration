package com.bms.enterpriseconfiguration.configuration.component.spring.bean.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.bms.enterpriseconfiguration.configuration.Configuration;
import com.bms.enterpriseconfiguration.configuration.component.ComponentConfigurationResolver;
import com.bms.enterpriseconfiguration.configuration.component.spring.annotation.CombinedClasspathConfiguration;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class CombinedClasspathConfigurationInjectionAnnotationBeanPostProcessor implements MergedBeanDefinitionPostProcessor, PriorityOrdered {

	protected transient Log logger = LogFactory.getLog(getClass());
	
	private Class<? extends Annotation> componentConfigurationAnnotationType;
	
	private int order = Ordered.LOWEST_PRECEDENCE;
	
	private transient final Map<Class<?>, Collection<ConfigurationElement>> metadataCache = Maps.newHashMap();
	
	public CombinedClasspathConfigurationInjectionAnnotationBeanPostProcessor(){
		this.componentConfigurationAnnotationType = CombinedClasspathConfiguration.class;
	}
	
	@Override
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public Class<? extends Annotation> getComponentConfigurationAnnotationType() {
		return componentConfigurationAnnotationType;
	}

	public void setComponentConfigurationAnnotationType(Class<? extends Annotation> componentConfigurationAnnotationType) {
		this.componentConfigurationAnnotationType = componentConfigurationAnnotationType;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Collection<ConfigurationElement> metadata = findMetadata(bean.getClass());
		try {
			for(ConfigurationElement element : metadata){
				element.set(bean, getConfiguration(element));
			}
		}
		catch (InvocationTargetException ex) {
			throw new BeanCreationException(beanName, "Injection of component configuration failed", ex.getTargetException());
		}
		catch (Throwable ex) {
			throw new BeanCreationException(beanName, "Couldn't inject component configuration", ex);
		}
		return bean;
	}
	
	private Configuration<?> getConfiguration(ConfigurationElement element) {
		CombinedClasspathConfiguration annotation = (CombinedClasspathConfiguration) element.getField().getAnnotation(this.componentConfigurationAnnotationType);
		String componentName = "";
		String configurationName = "";
		if(!Strings.isNullOrEmpty(annotation.value())){
			if(!annotation.value().contains(">")){
				throw new RuntimeException("The value should be set to componentName>configurationName or the componentName and configurationName elements should be used instead!");
			}
			componentName = annotation.value().substring(0, annotation.value().indexOf('>'));
			configurationName = annotation.value().substring(annotation.value().indexOf('>') + 1);
		}else{
			componentName = annotation.componentName();
			configurationName = annotation.name();
		}
		String environmentName = annotation.environment();
		if(Strings.isNullOrEmpty(environmentName)){
			environmentName = ComponentConfigurationResolver.DEFAULT_ENVIRONMENT;
		}
		return (Configuration<?>) Enhancer.create(Configuration.class, new CombinedClasspathConfigurationMethodInterceptor(componentName, configurationName, environmentName));
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	@Override
	public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
		if (beanType != null) {
			findMetadata(beanType);
		}
	}
	
	private Collection<ConfigurationElement> findMetadata(Class<?> clazz){
		if (this.metadataCache == null) {
			// Happens after deserialization, during destruction...
			return buildMetadata(clazz);
		}
		Collection<ConfigurationElement> metadata = this.metadataCache.get(clazz);
		if (metadata == null) {
			synchronized (this.metadataCache) {
				metadata = this.metadataCache.get(clazz);
				if (metadata == null) {
					metadata = buildMetadata(clazz);
					this.metadataCache.put(clazz, metadata);
				}
				return metadata;
			}
		}
		return metadata;
	}
	
	private Collection<ConfigurationElement> buildMetadata(Class<?> clazz){
		final boolean debug = logger.isDebugEnabled();
		LinkedList<ConfigurationElement> autowiredComponentConfigurations = Lists.newLinkedList();
		Class<?> targetClass = clazz;

		do {
			LinkedList<ConfigurationElement> currentAutowiredComponentConfigurations = Lists.newLinkedList();
			
			for (Field field : targetClass.getDeclaredFields()) {
				if (this.componentConfigurationAnnotationType != null) {
					if (field.getAnnotation(this.componentConfigurationAnnotationType) != null) {
						ConfigurationElement element = new ConfigurationElement(field);
						currentAutowiredComponentConfigurations.add(element);
						if (debug) {
							logger.debug("Found injectible configuration field on class [" + clazz.getName() + "]: " + field);
						}
					}
				}
			}
			autowiredComponentConfigurations.addAll(0, currentAutowiredComponentConfigurations);
			targetClass = targetClass.getSuperclass();
		}
		while (targetClass != null && targetClass != Object.class);

		return autowiredComponentConfigurations;
	}
	
	private static class ConfigurationElement {
		
		private final Field field;

		public ConfigurationElement(Field field) {
			this.field = field;
		}

		public Field getField() {
			return field;
		}
		
		public void set(Object target, Object value) throws Throwable{
			ReflectionUtils.makeAccessible(this.field);
			this.field.set(target, value);
		}
		
	}

}
