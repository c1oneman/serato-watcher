package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Bwll extends ByteConsumer {

	public Bwll() {
		super();
		id = name = "bwll";
		
		consume = new BooleanConsumeStrategy();	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Bwll();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
