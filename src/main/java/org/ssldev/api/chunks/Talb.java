package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database album title
 */
public class Talb extends ByteConsumer{

	public Talb() {
		super();
		id = name = "talb";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Talb();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
