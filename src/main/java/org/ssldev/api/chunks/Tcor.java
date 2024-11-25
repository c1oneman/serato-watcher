package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database grouping
 */
public class Tcor extends ByteConsumer{

	public Tcor() {
		super();
		id = name = "tcor";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tcor();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
