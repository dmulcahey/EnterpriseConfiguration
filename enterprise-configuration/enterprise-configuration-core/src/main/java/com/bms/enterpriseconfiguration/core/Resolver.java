package com.bms.enterpriseconfiguration.core;

public interface Resolver<INPUT,OUTPUT> {

	OUTPUT resolve(INPUT input);
	
}
