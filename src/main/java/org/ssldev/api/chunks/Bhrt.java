package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Bhrt extends ByteConsumer {

	public Bhrt() {
		super();
		id = name = "bhrt";
		
		consume = new BooleanConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Bhrt();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
