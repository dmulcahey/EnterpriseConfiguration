package com.bms.enterpriseconfiguration.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Ignore;
import org.junit.Test;

public class ResolverTest {
	
	@Test
	@Ignore
	public void testResolver(){
		AbstractResolver<String,String> resolver = mock(AbstractResolver.class);
		assertNotNull(resolver.getPostresolutionActivities());
		assertNotNull(resolver.getPostresolutionTests());
		assertNotNull(resolver.getPreresolutionActivities());
		assertNotNull(resolver.getPreresolutionTests());
		
		resolver.addPostresolutionActivity(mock(ResolutionActivity.class, "MockPostresolutionActivity"));
		resolver.addPreresolutionActivity(mock(ResolutionActivity.class, "MockPreresolutionActivity"));
		resolver.addPostresolutionTest(mock(ResolutionTest.class, "MockPostresolutionTest"));
		resolver.addPreresolutionTest(mock(ResolutionTest.class, "MockPreresolutionTest"));
		
		assertTrue(resolver.getPostresolutionActivities().size() == 1);
		assertTrue(resolver.getPreresolutionActivities().size() == 1);
		assertTrue(resolver.getPostresolutionTests().size() == 1);
		assertTrue(resolver.getPreresolutionTests().size() == 1);
		
		reset(resolver);
		
		resolver.addPostresolutionActivity(mock(ResolutionActivity.class, "MockPostresolutionActivity"));
		resolver.addPreresolutionActivity(mock(ResolutionActivity.class, "MockPreresolutionActivity"));
		resolver.addPostresolutionTest(mock(ResolutionTest.class, "MockPostresolutionTest"));
		resolver.addPreresolutionTest(mock(ResolutionTest.class, "MockPreresolutionTest"));
		
		when(resolver.doResolution("Test")).thenReturn("Resolved Test");
		
		String result = resolver.resolve("Test");
		
		verify(resolver, times(1)).doResolution("Test");
		
		assertEquals("Resolved Test", result);
	}

}
