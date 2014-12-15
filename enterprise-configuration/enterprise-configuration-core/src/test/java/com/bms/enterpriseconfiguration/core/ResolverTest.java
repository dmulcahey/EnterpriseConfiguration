package com.bms.enterpriseconfiguration.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ResolverTest {
	
	@Test
	public void testResolver(){
		AbstractResolver<String,String> resolver = mock(AbstractResolver.class);
		assertNotNull(resolver.getPostresolutionActivities());
		assertNotNull(resolver.getPostresolutionTests());
		assertNotNull(resolver.getPreresolutionActivities());
		assertNotNull(resolver.getPreresolutionTests());
		
		resolver.addPostresolutionActivity(mock(ResolutionActivity.class));
		resolver.addPreresolutionActivity(mock(ResolutionActivity.class));
		resolver.addPostresolutionTest(mock(ResolutionTest.class));
		resolver.addPreresolutionTest(mock(ResolutionTest.class));
		
		assertTrue(resolver.getPostresolutionActivities().size() == 1);
		assertTrue(resolver.getPreresolutionActivities().size() == 1);
		assertTrue(resolver.getPostresolutionTests().size() == 1);
		assertTrue(resolver.getPreresolutionTests().size() == 1);
		
		reset(resolver);
		
		resolver.addPostresolutionActivity(mock(ResolutionActivity.class));
		resolver.addPreresolutionActivity(mock(ResolutionActivity.class));
		resolver.addPostresolutionTest(mock(ResolutionTest.class));
		resolver.addPreresolutionTest(mock(ResolutionTest.class));
		
		when(resolver.doResolution("Test")).thenReturn("Resolved Test");
		
		String result = resolver.resolve("Test");
		
		verify(resolver, times(1)).doResolution("Test");
		
		assertEquals("Resolved Test", result);
	}

}
