package com.bms.enterpriseconfiguration.configuration.component.spring;

import static org.junit.Assert.assertNotNull;

import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.bms.enterpriseconfiguration.configuration.component.spring.annotation.EnableConfigurationInjection;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=InjectCombinedClasspathConfigurationTest.class)
@Configuration
@EnableConfigurationInjection
@ComponentScan(basePackages={"com.bms.enterpriseconfiguration.configuration.component.spring"})
@TestExecutionListeners({DirtiesContextTestExecutionListener.class, 
    DependencyInjectionTestExecutionListener.class})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class InjectCombinedClasspathConfigurationTest {
	
	@Autowired
	private BeanWithInjectedConfiguration beanWithInjectedConfiguration;
	
	@Autowired
	ApplicationContext context;
	
	@Test
	public void testComponentConfigurationInjected(){		
		assertNotNull(beanWithInjectedConfiguration);
		assertNotNull(beanWithInjectedConfiguration.getConfiguration());
		assertNotNull(beanWithInjectedConfiguration.getResource());
		
		Logger.getAnonymousLogger().info(beanWithInjectedConfiguration.toString());
	}
	
}
