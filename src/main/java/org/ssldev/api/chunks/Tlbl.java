package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database grouping
 */
public class Tlbl extends ByteConsumer{

	public Tlbl() {
		super();
		id = name = "tlbl";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tlbl();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
