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

import com.bms.enterpriseconfiguration.configuration.component.ComponentConfigurationResolver;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class ClasspathResourceInjectionAnnotationBeanPostProcessor  implements MergedBeanDefinitionPostProcessor, PriorityOrdered {

	protected transient Log logger = LogFactory.getLog(getClass());

	private Class<? extends Annotation> configurationResourceAnnotationType;

	private int order = Ordered.LOWEST_PRECEDENCE;

	private transient final Map<Class<?>, Collection<ConfigurationElement>> metadataCache = Maps.newHashMap();
	
	public ClasspathResourceInjectionAnnotationBeanPostProcessor() {
		this.configurationResourceAnnotationType = com.bms.enterpriseconfiguration.configuration.component.spring.annotation.ClasspathResource.class;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Class<? extends Annotation> getConfigurationResourceAnnotationType() {
		return configurationResourceAnnotationType;
	}

	public void setConfigurationResourceAnnotationType(Class<? extends Annotation> configurationResourceAnnotationType) {
		this.configurationResourceAnnotationType = configurationResourceAnnotationType;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Collection<ConfigurationElement> metadata = findMetadata(bean.getClass());
		try {
			for (ConfigurationElement element : metadata) {
				element.set(bean, getConfigurationResource(element));
			}
		} catch (InvocationTargetException ex) {
			throw new BeanCreationException(beanName, "Injection of component configuration failed", ex.getTargetException());
		} catch (Throwable ex) {
			throw new BeanCreationException(beanName, "Couldn't inject component configuration", ex);
		}
		return bean;
	}

	private ClasspathResource getConfigurationResource(ConfigurationElement element) {
		com.bms.enterpriseconfiguration.configuration.component.spring.annotation.ClasspathResource annotation = (com.bms.enterpriseconfiguration.configuration.component.spring.annotation.ClasspathResource) element.getField().getAnnotation(this.configurationResourceAnnotationType);
		String componentName = "";
		String configurationResourceName = "";
		if(!Strings.isNullOrEmpty(annotation.value())){
			if(!annotation.value().contains(">")){
				throw new RuntimeException("The value should be set to componentName>configurationName or the componentName and configurationName elements should be used instead!");
			}
			componentName = annotation.value().substring(0, annotation.value().indexOf('>'));
			configurationResourceName = annotation.value().substring(annotation.value().indexOf('>') + 1);
		}else{
			componentName = annotation.componentName();
			configurationResourceName = annotation.name();
		}
		String environmentName = annotation.environment();
		if(Strings.isNullOrEmpty(environmentName)){
			environmentName = ComponentConfigurationResolver.DEFAULT_ENVIRONMENT;
		}
		return (ClasspathResource) Enhancer.create(ClasspathResource.class, new ClasspathResourceMethodInterceptor(componentName, configurationResourceName, environmentName));
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

	private Collection<ConfigurationElement> findMetadata(Class<?> clazz) {
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

	private Collection<ConfigurationElement> buildMetadata(Class<?> clazz) {
		final boolean debug = logger.isDebugEnabled();
		LinkedList<ConfigurationElement> autowiredConfigurationResources = Lists.newLinkedList();
		Class<?> targetClass = clazz;

		do {
			LinkedList<ConfigurationElement> currentAutowiredConfigurationResources = Lists.newLinkedList();

			for (Field field : targetClass.getDeclaredFields()) {
				if (this.configurationResourceAnnotationType != null) {
					if (field.getAnnotation(this.configurationResourceAnnotationType) != null) {
						ConfigurationElement element = new ConfigurationElement(field);
						currentAutowiredConfigurationResources.add(element);
						if (debug) {
							logger.debug("Found injectible configuration field on class [" + clazz.getName() + "]: " + field);
						}
					}
				}
			}
			autowiredConfigurationResources.addAll(0, currentAutowiredConfigurationResources);
			targetClass = targetClass.getSuperclass();
		} while (targetClass != null && targetClass != Object.class);

		return autowiredConfigurationResources;
	}

	private static class ConfigurationElement {

		private final Field field;

		public ConfigurationElement(Field field) {
			this.field = field;
		}

		public Field getField() {
			return field;
		}

		public void set(Object target, Object value) throws Throwable {
			ReflectionUtils.makeAccessible(this.field);
			this.field.set(target, value);
		}

	}

}
