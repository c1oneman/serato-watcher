package org.ssldev.api.consumption.strategies;

import static org.ssldev.api.utils.BinaryUtil.intToBytes;

import org.ssldev.api.consumption.SslBuffer;
import org.ssldev.core.utils.Logger;

public class IntConsumeStrategy extends ConsumeStrategy<Integer> {
	@Override
	public void consume(SslBuffer buf) {
		this.bytes = new SslBuffer(buf);
		// length
		length = buf.size();
		Logger.finest(this, "length " + length);
		if(length != 4) throw new IllegalStateException("expected int length to be 4. actual is "+length+".  prob wrong consume strategy");

		// value
		this.data = buf.readInt4();
	}

	@Override
	public Integer defaultValue() {
		return new Integer(-1);
	}

	@Override
	public void setData(Integer val) {
		bytes.clear();
		bytes.add(intToBytes(val));
		data = val;
	}
}
