package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.StringConsumeStrategy;
import org.ssldev.core.utils.StringUtils;

/**
 * database added.
 * TODO: has time zone dependency
 */
public class Tadd extends ByteConsumer{

	public Tadd() {
		super();
		id = name = "tadd";
		
		consume = new StringConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Tadd();
	}	
	
	@Override
	public String toString() {
		return  "["+id+"]: ["+StringUtils.toDateAndTime( Long.valueOf(data.toString()) * 1000) + "]";
	}

	@Override
	public String getData() {
		return (String) data;
	}
	
}
