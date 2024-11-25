package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Bcrt extends ByteConsumer {

	public Bcrt() {
		super();
		id = name = "bcrt";
		
		consume = new BooleanConsumeStrategy();	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Bcrt();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
