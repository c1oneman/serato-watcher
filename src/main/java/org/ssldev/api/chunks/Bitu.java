package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Bitu extends ByteConsumer {

	public Bitu() {
		super();
		id = name = "bitu";
		
		consume = new BooleanConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Bitu();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
}
