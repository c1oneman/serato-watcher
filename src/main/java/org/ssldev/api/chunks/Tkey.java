package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;
/**
 * key 
 */
public class Tkey extends ByteConsumer {
	public Tkey() {
		super();
		id = name = "tkey";
		
		consume = new StringConsumeStrategy();
	}

	@Override
	public ByteConsumer getInstance() {
		return new Tkey();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
