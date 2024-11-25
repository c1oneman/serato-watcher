package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

public class Sbav extends ByteConsumer {


	public Sbav() {
		super();
		id = name = "sbav";
		
		consume = new StringConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Sbav();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
}
