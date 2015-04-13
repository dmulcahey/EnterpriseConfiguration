package com.bms.enterpriseconfiguration.core;

public interface Ordered {

	static final int HIGHEST_PRECEDENCE = Integer.MAX_VALUE;
	static final int LOWEST_PRECEDENCE = Integer.MIN_VALUE;
	
	int getOrder();
	
}
