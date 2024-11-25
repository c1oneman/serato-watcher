package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Bovc extends ByteConsumer {

	public Bovc() {
		super();
		id = name = "bovc";
		
		consume = new BooleanConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Bovc();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
