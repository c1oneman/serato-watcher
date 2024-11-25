package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Buns extends ByteConsumer {

	public Buns() {
		super();
		id = name = "buns";
		
		consume = new BooleanConsumeStrategy();	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Buns();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
