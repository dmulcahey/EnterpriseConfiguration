package com.bms.enterpriseconfiguration.core;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestClasspathScanning.class)
@Configuration
public class TestClasspathScanning {

	@Value("classpath*:ComponentResources/**/*.*")
	private Resource[] componentResources;
	
	@Value("classpath*:SharedResources/**/*.*")
	private Resource[] sharedResources;
	
	@Value("classpath*:SecureResources/#{systemProperties['server.env']}/**/*.*")
	private Resource[] secureResources;
	
	@Value("classpath*:EnvironmentResources/#{systemProperties['server.env']}/**/*.*")
	private Resource[] environmentResources;
	
	@BeforeClass
	public static void setup(){
		System.setProperty("server.env", "JUNIT");
	}
	
	@Test
	public void testComponentResources() throws IOException{
		assertNotNull(componentResources);
		for(Resource resource : componentResources){
			String path = resource.getURL().toExternalForm();
			Logger.getAnonymousLogger().info(path.substring(path.lastIndexOf("/") + 1));
			
			String[] segments = resource.getURL().toExternalForm().split("/");
			boolean next = false;
			for(String segment : segments){
				if(next){
					next = false;
					Logger.getAnonymousLogger().info("Component Name = " + segment);
				}else{
					if(segment.equalsIgnoreCase("ComponentResources")){
						next = true;
					}
				}
			}
			
			
		}
		
		assertNotNull(sharedResources);
		for(Resource resource : sharedResources){
			Logger.getAnonymousLogger().info(resource.getURL().toExternalForm());
		}
		
		assertNotNull(secureResources);
		for(Resource resource : secureResources){
			Logger.getAnonymousLogger().info(resource.getURL().toExternalForm());
		}
		
		assertNotNull(environmentResources);
		for(Resource resource : environmentResources){
			Logger.getAnonymousLogger().info(resource.getURL().toExternalForm());
		}
	}
	
	
}
