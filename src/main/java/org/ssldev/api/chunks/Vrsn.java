package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * 1: id: 'vrsn'
 * 2: version field
 */
public class Vrsn extends ByteConsumer {
	/**
	 * ex: 1.0/Serato Scratch LIVE Review
	 */
//	public String version;
	
	public Vrsn() {
		super();
		id = name = "vrsn";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Vrsn();
	}

	@Override
	public String getData() {
		return (String) data;
	}
	
}
