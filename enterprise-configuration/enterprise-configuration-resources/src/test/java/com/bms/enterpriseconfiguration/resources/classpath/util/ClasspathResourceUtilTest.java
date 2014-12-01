package com.bms.enterpriseconfiguration.resources.classpath.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

public class ClasspathResourceUtilTest {

	@Test
	public void testGetSubdirectoryNamesFromParentDirectory() throws IOException{
		Set<String> subdirectories = ClasspathResourceUtil.getSubdirectoryNamesFromParentDirectory("ComponentResources");
		Logger.getAnonymousLogger().info(subdirectories.toString());
		assertEquals(2, subdirectories.size());
		assertTrue(subdirectories.contains("SomeComponent"));
		assertTrue(subdirectories.contains("ConfigurationCore"));
	}
	
}
