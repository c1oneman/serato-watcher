package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class Bwlb extends ByteConsumer {

	public Bwlb() {
		super();
		id = name = "bwlb";
		
		consume = new BooleanConsumeStrategy();	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Bwlb();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
