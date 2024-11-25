package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database BPM 
 */
public class Tbpm extends ByteConsumer{

	public Tbpm() {
		super();
		id = name = "tbpm";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tbpm();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
