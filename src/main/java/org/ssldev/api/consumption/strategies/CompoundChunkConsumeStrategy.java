package org.ssldev.api.consumption.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.ssldev.api.chunks.UnknownUnk;
import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.ByteConsumerIF;
import org.ssldev.api.consumption.IntString;
import org.ssldev.api.consumption.SslBuffer;
import org.ssldev.api.utils.BinaryUtil;
import org.ssldev.core.utils.Logger;

public class CompoundChunkConsumeStrategy extends ConsumeStrategy<List<ByteConsumerIF>> {

	private ByteConsumer consumer;
	private List<ByteConsumerIF> consumers = new ArrayList<>();
	
	
	public CompoundChunkConsumeStrategy(ByteConsumer c) {
		consumer = c;
	}
	
	public void consume(SslBuffer bytes) {
		length = bytes.size();
		this.bytes = new SslBuffer();
		
		while(!bytes.isEmpty()) {
			// key may be int or string interpretation 
			IntString key = new IntString(bytes.remove(4));
			int len = bytes.readInt4();
			SslBuffer buf = bytes.remove(len);
			
			if(consumer.isRegistered(key)) {
				ByteConsumerIF subConsumer = consumer.buildConsumer(key);
				subConsumer.consume(buf);
				consumers.add(subConsumer);
			} else {
				UnknownUnk unknownChunk = consumeUnknownChunkId(key, buf);
				consumer.register(unknownChunk);
				consumers.add(unknownChunk);
			}
			
			Logger.finest(this, consumer.getName() + ": buffer size now " + bytes.size());
		}
		data = consumers;
	}
	
	private UnknownUnk consumeUnknownChunkId(IntString key, SslBuffer bytes) {
		Logger.warn(this, "encountered unknown registration id: " + key);
		UnknownUnk u = new UnknownUnk(key.getIntVal()); 
		u.consume(bytes); // TODO should this be published instead of tossed?
		return u;
	}
	
	@Override
	public String toString() {
		return super.toString() + "\nbelongs to " + consumer.getName();
	}

	@Override
	public List<ByteConsumerIF> defaultValue() {
		return Collections.<ByteConsumerIF>emptyList();
	}

	@Override
	public List<Integer> toBytes() {
		//TODO this only works if the consumers order is maintained on consumption
		List<Integer> res = new LinkedList<>();
		for(ByteConsumerIF b : consumers) {
			String consumerId = b.getId();
			int consumeLength = b.size();

			// add 4 bytes consumer ID
			res.addAll(BinaryUtil.stringToBytes(consumerId));
			// add 4 bytes consumed length
			res.addAll(BinaryUtil.intToBytes(consumeLength));
			// add consumed data up to consumed length
			res.addAll(b.toBytes());
		}
		
		return res;
	}

	@Override
	public void setData(List<ByteConsumerIF> val) {
		consumers.clear();
		consumers.addAll(val);
		data = val;
	}
	
}
