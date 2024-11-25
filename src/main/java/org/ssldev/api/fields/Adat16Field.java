package org.ssldev.api.fields;

import org.ssldev.api.consumption.strategies.LongOrIntConsumeStrategy;

public class Adat16Field extends Field <Adat16Field >{

	public Adat16Field() {
		super("field16",16);
		consume = new LongOrIntConsumeStrategy();
	}
	
}
