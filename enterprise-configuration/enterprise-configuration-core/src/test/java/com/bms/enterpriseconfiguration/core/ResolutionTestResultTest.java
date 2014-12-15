package com.bms.enterpriseconfiguration.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolutionTestResultTest {
	
	@Test
	public void testResolutionTestResult(){
		ResolutionTestResult resolutionTestResult = new ResolutionTestResult();
		
		assertFalse(resolutionTestResult.getErrorMessage().isPresent());
		assertTrue(resolutionTestResult.isSuccessful());
		
		resolutionTestResult.setErrorMessage("Test Error Message");
		
		assertTrue(resolutionTestResult.getErrorMessage().isPresent());
		assertFalse(resolutionTestResult.isSuccessful());
		assertEquals("Test Error Message", resolutionTestResult.getErrorMessage().get());
		assertFalse(resolutionTestResult.getInformationMessage().isPresent());
		assertFalse(resolutionTestResult.getPossibleException().isPresent());
		assertFalse(resolutionTestResult.getWarningMessage().isPresent());
		
		resolutionTestResult.setInformationMessage("Test Information Message");
		resolutionTestResult.setWarningMessage("Test Warning Message");
		Exception exception = new Exception();
		resolutionTestResult.setPossibleException(exception);
		
		assertTrue(resolutionTestResult.getInformationMessage().isPresent());
		assertTrue(resolutionTestResult.getPossibleException().isPresent());
		assertTrue(resolutionTestResult.getWarningMessage().isPresent());
		assertEquals("Test Information Message", resolutionTestResult.getInformationMessage().get());
		assertEquals("Test Warning Message", resolutionTestResult.getWarningMessage().get());
		assertEquals(exception, resolutionTestResult.getPossibleException().get());
	}

}
