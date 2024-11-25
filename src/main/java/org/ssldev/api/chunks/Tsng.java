package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database song title
 */
public class Tsng extends ByteConsumer{

	public Tsng() {
		super();
		id = name = "tsng";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tsng();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
