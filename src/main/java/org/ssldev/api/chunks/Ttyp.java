package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database track type (e.g. mp3)
 */
public class Ttyp extends ByteConsumer{

	public Ttyp() {
		super();
		id = name = "ttyp";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Ttyp();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
}
