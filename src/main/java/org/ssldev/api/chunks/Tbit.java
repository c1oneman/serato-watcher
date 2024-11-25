package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database bit rate 
 */
public class Tbit extends ByteConsumer{

	public Tbit() {
		super();
		id = name = "tbit";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tbit();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
