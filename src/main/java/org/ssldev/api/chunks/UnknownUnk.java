package org.ssldev.api.chunks;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.ByteConsumerIF;
import org.ssldev.api.consumption.strategies.RawStringConsumeStrategy;

public class UnknownUnk extends ByteConsumer {
	private String data;
	
	public UnknownUnk() {
		super();
		name = "Unknown Chunk";
		id = "unkn";
		consume = new RawStringConsumeStrategy();
	}
	public UnknownUnk(String id) {
		super();
		this.id = id;
		name = "unknown ID " + id;
		consume = new RawStringConsumeStrategy();
	}
	
	public String getData() {
		return data;
	}

	@Override
	public ByteConsumerIF getInstance() {
		return new UnknownUnk();
	}

}
