package com.bms.enterpriseconfiguration.configuration.component.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.bms.enterpriseconfiguration.configuration.component.spring.bean.processor.InjectorConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(InjectorConfiguration.class)
public @interface EnableConfigurationInjection {

}
