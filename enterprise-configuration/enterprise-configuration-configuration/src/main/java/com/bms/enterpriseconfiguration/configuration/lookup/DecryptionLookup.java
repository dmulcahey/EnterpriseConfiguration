package com.bms.enterpriseconfiguration.configuration.lookup;

import org.apache.commons.configuration2.interpol.Lookup;
import org.jasypt.util.text.StrongTextEncryptor;

public class DecryptionLookup implements Lookup {

	@Override
	public Object lookup(String variable) {
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPassword("TESTING");
		return textEncryptor.decrypt(variable);
	}

}
