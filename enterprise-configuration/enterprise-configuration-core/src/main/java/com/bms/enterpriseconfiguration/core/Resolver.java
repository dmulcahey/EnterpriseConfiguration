package com.bms.enterpriseconfiguration.core;

public interface Resolver<I,O> {

	O resolve(I input);
	
}
