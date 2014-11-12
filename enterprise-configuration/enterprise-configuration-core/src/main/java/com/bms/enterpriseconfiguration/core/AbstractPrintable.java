package com.bms.enterpriseconfiguration.core;

import com.bms.enterpriseconfiguration.core.util.PrintFormatter;

public abstract class AbstractPrintable {

	@Override
	public String toString() {
		return PrintFormatter.format(this);
	}

}
