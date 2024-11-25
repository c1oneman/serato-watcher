package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

/**
 * serato crate sorting reversed (ascending or descending) flag
 */
public class Brev extends ByteConsumer{

	public Brev() {
		super();
		id = name = "brev";
		
		consume = new BooleanConsumeStrategy();;	
	}

	@Override
	public ByteConsumer getInstance() {
		return new Brev();
	}

	@Override
	public Boolean getData() {
		return Boolean.valueOf((boolean) data);
	}
	
}
