package com.bms.enterpriseconfiguration.configuration.component.spring.bean.processor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@ComponentScan(basePackages="com.bms.enterpriseconfiguration.configuration.component.spring.bean.processor")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InjectorConfiguration {

}
