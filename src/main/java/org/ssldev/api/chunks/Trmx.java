package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database remix tag
 */
public class Trmx extends ByteConsumer{

	public Trmx() {
		super();
		id = name = "trmx";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Trmx();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
