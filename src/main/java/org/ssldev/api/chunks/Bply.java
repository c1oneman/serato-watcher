package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Bply extends ByteConsumer {

	public Bply() {
		super();
		id = name = "bply";
		
		consume = new BooleanConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Bply();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
