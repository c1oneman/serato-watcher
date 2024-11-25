package org.ssldev.api.fields;

import org.ssldev.api.consumption.strategies.IntConsumeStrategy;

public class Adat48SessionIdField extends Field <Adat48SessionIdField>{

	public static final String ID = "48";
	
	public Adat48SessionIdField() {
		super("Session ID",48);
		consume = new IntConsumeStrategy();
	}

}
