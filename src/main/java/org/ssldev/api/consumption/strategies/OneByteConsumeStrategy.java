	package org.ssldev.api.consumption.strategies;

import static org.ssldev.api.utils.BinaryUtil.intToBytes;

import org.ssldev.api.consumption.SslBuffer;

public class OneByteConsumeStrategy extends ConsumeStrategy<Integer> {
	
	@Override
	public void consume(SslBuffer buf) {
		this.bytes = new SslBuffer(buf);
		length = buf.size(); 
		if(length != 1) throw new IllegalStateException("assumed always length of 1. instead length == " +length);
		this.data = buf.poll();
	}

	@Override
	public Integer defaultValue() {
		return -1;
	}

	@Override
	public void setData(Integer val) {
		bytes.clear();
		bytes.add(intToBytes(val));
		data = val;
	}

}
