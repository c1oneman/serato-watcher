package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database grouping
 */
public class Pvid extends ByteConsumer{

	public Pvid() {
		super();
		id = name = "pvid";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Pvid();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
