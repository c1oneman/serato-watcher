package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database file pointer.  data gets the absolute path of a file
 */
public class Pfil extends ByteConsumer{

	public Pfil() {
		super();
		id = name = "pfil";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Pfil();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
