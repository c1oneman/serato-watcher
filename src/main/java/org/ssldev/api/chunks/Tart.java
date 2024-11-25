package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database artist title
 */
public class Tart extends ByteConsumer{

	public Tart() {
		super();
		id = name = "tart";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tart();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
