package com.bms.enterpriseconfiguration.configuration.component.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClasspathResource {

	/**
	 * Declares whether the annotated dependency is required.
	 * <p>Defaults to {@code true}.
	 */
	boolean required() default true;
	
	String environment() default "";
	
	String componentName() default "";
	
	String name() default "";
	
	String value() default "";
	
}
