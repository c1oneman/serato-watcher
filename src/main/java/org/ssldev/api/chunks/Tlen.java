package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database song length
 */
public class Tlen extends ByteConsumer{

	public Tlen() {
		super();
		id = name = "tlen";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tlen();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
