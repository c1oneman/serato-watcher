package org.ssldev.api.consumption.strategies;

import static org.ssldev.api.utils.BinaryUtil.stringToBytes;

import org.ssldev.api.consumption.SslBuffer;

public class RawStringConsumeStrategy extends ConsumeStrategy<String> {

	@Override
	public void consume(SslBuffer buf) {
		bytes = new SslBuffer(buf);
		length = buf.size();
		data = buf.readRawString(buf.size());
	}

	@Override
	public String defaultValue() {
		return "";
	}

	@Override
	public void setData(String val) {
		bytes.clear();
		bytes.add(stringToBytes(val));
		data = val;
	}
}
