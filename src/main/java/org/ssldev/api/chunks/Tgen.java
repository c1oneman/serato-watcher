package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database genre title
 */
public class Tgen extends ByteConsumer{

	public Tgen() {
		super();
		id = name = "tgen";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tgen();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
