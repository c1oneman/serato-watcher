package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;
/**
 * year
 */
public class Ttyr extends ByteConsumer {

	public Ttyr() {
		super();
		id = name = "ttyr";
		
		consume = new StringConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Ttyr();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
