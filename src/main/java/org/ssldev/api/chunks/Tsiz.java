package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database size of track
 */
public class Tsiz extends ByteConsumer{

	public Tsiz() {
		super();
		id = name = "tsiz";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tsiz();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
