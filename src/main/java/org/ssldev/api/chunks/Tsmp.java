package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database sampling rate
 */
public class Tsmp extends ByteConsumer{

	public Tsmp() {
		super();
		id = name = "tsmp";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tsmp();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
