package com.bms.enterpriseconfiguration.configuration.component.management;

import static org.junit.Assert.assertEquals;

import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.Test;

import com.bms.enterpriseconfiguration.configuration.component.ComponentConfiguration;
import com.bms.enterpriseconfiguration.configuration.component.ComponentConfigurationManager;

public class ComponentConfigurationManagerMXBeanImplTest {

	
	@Test
	public void testLogComponentConfiguration() throws Exception{
		@SuppressWarnings("unused")
		ComponentConfiguration componentConfiguration = ComponentConfigurationManager.getComponentConfiguration("Configuration", "JUNIT");
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi://");
		JMXConnectorServer jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, mBeanServer);
		jmxConnectorServer.start();
		JMXConnector jmxConnector = null;
		try {
		    jmxServiceURL = jmxConnectorServer.getAddress();
		    jmxConnector = JMXConnectorFactory.connect(jmxServiceURL);
		    MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();
	        ObjectName objectName = new ObjectName("com.bms.enterpriseconfiguration.configuration.component.management:type=ComponentConfigurationManagerMXBean");
	        assertEquals(16, mBeanServerConnection.getMBeanInfo(objectName).getOperations().length);
	        for(MBeanOperationInfo mBeanOperationInfo : mBeanServerConnection.getMBeanInfo(objectName).getOperations()){
	        	Logger.getAnonymousLogger().info("Operation: " + mBeanOperationInfo.getName());
	        }
	        
	        mBeanServerConnection.invoke(objectName, "logComponentConfiguration", new Object[]{"Configuration","JUNIT"}, new String[]{String.class.getName(), String.class.getName()});
		} finally {
		    if (jmxConnector != null){
		    	jmxConnector.close();
		    }
		    jmxConnectorServer.stop();
		}
	}
}
