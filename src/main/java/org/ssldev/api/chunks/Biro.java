package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Biro extends ByteConsumer {

	public Biro() {
		super();
		id = name = "biro";
		
		consume = new BooleanConsumeStrategy();	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Biro();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
