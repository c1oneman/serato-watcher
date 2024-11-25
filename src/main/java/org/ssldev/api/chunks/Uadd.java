package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.LongOrIntConsumeStrategy;
import org.ssldev.core.utils.StringUtils;

/**
 */
public class Uadd extends ByteConsumer{

	public Uadd() {
		super();
		id = name = "uadd";
		
		consume = new LongOrIntConsumeStrategy();
	}
	
	@Override
	public ByteConsumer getInstance() {
		return new Uadd();
	}
	
	@Override
	public String getData() {
		return (String) data;
	}
	
	@Override
	public String toString() {
		return  "["+id+"]: ["+StringUtils.toDateAndTime( Long.valueOf(data.toString()) * 1000) + "]";
	}
}
