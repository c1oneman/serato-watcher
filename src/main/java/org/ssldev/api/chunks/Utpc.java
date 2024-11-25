package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.LongOrIntConsumeStrategy;
/**
 * play count
 */
public class Utpc extends ByteConsumer {

	public Utpc() {
		super();
		id = name = "utpc";
		
		consume = new LongOrIntConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Utpc();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
