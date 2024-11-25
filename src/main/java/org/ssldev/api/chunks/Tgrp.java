package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

/**
 * database grouping
 */
public class Tgrp extends ByteConsumer{

	public Tgrp() {
		super();
		id = name = "tgrp";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tgrp();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}

}
