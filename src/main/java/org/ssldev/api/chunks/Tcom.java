package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;
/**
 * comment
 */
public class Tcom extends ByteConsumer {

	public Tcom() {
		super();
		id = name = "tcom";
		
		consume = new StringConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Tcom();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
