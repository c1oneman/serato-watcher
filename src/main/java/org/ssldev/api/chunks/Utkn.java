package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.LongOrIntConsumeStrategy;

public class Utkn extends ByteConsumer {

	public Utkn() {
		super();
		id = name = "utkn";
		
		consume = new LongOrIntConsumeStrategy();	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Utkn();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
