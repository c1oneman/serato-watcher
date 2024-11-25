package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.LongOrIntConsumeStrategy;
/**
 * ex: len 4; 255 255 117
 */
public class Ulbl extends ByteConsumer {

	public Ulbl() {
		super();
		id = name = "ulbl";
		
		//TODO strategy for session/crate used to be string.  verify compatible strategy for session/crate conversions
		consume = new LongOrIntConsumeStrategy();
	}

	@Override
	public ByteConsumer getInstance() {
		return new Ulbl();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
