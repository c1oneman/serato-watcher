package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Blop extends ByteConsumer {

	public Blop() {
		super();
		id = name = "blop";
		
		consume = new BooleanConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Blop();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
