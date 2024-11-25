package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 */
public class Udsc extends ByteConsumer{

	public Udsc() {
		super();
		id = name = "udsc";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Udsc();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
