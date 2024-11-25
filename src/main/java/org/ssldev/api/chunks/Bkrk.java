package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Bkrk extends ByteConsumer {

	public Bkrk() {
		super();
		id = name = "bkrk";
		
		consume = new BooleanConsumeStrategy();	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Bkrk();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
