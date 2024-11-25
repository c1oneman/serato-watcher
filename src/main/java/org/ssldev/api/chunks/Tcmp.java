package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;
/**
 * composer info
 */
public class Tcmp extends ByteConsumer {

	public Tcmp() {
		super();
		id = name = "tcmp";
		
		consume = new StringConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Tcmp();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
}
