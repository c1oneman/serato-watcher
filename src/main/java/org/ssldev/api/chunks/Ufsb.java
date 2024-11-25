package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.LongOrIntConsumeStrategy;

/**
 */
public class Ufsb extends ByteConsumer{

	public Ufsb() {
		super();
		id = name = "ufsb";
		
		consume = new LongOrIntConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Ufsb();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
