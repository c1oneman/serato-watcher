package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Bbgl extends ByteConsumer {

	public Bbgl() {
		super();
		id = name = "bbgl";
		
		consume = new BooleanConsumeStrategy();	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Bbgl();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
