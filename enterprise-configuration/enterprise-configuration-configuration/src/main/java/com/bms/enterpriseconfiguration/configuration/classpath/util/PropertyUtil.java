package com.bms.enterpriseconfiguration.configuration.classpath.util;

public class PropertyUtil {

	public static final String PREFIX = "PU";
	public static final String ENVIRONMENT_JVM_ARGUMENT = "server.env";
	public static final String ENVIRONMENT_JVM_ARGUMENT_ALIAS = "env.name";
	
	private PropertyUtil(){
	}
	
	public static String clearZeroFromEnv(String environment){
		if(environment.contains("PD0")){
			return environment.replace("PD0", "PD");
		}else if(environment.contains("PS0")){
			return environment.replace("PS0", "PS");
		}else if(environment.contains("PMD0")){
			return environment.replace("PMD0", "PMD");
		}else if(environment.contains("PMS0")){
			return environment.replace("PMS0", "PMS");
		}else{
			return environment;
		}
	}
	
	public static String getServerEnvironment(){
		if(System.getProperties().containsKey(ENVIRONMENT_JVM_ARGUMENT)){
			return System.getProperty(ENVIRONMENT_JVM_ARGUMENT);
		}else{
			return System.getProperty(ENVIRONMENT_JVM_ARGUMENT_ALIAS);
		}
	}
	
	public static String getServerEnvironmentNoZero(){
		return PropertyUtil.clearZeroFromEnv(PropertyUtil.getServerEnvironment());
	}
	
}