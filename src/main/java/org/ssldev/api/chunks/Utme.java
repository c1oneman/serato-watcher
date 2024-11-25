package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.LongOrIntConsumeStrategy;
import org.ssldev.core.utils.StringUtils;
/**
 * ex: len 4; 9377222226
 */
public class Utme extends ByteConsumer{

	public Utme() {
		super();
		id = name = "utme";
		
		consume = new LongOrIntConsumeStrategy();	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Utme();
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
