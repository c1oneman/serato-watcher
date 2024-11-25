package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Bmis extends ByteConsumer {

	public Bmis() {
		super();
		id = name = "bmis";
		
		consume = new BooleanConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Bmis();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
