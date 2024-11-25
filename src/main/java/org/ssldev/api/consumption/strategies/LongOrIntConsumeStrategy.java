package org.ssldev.api.consumption.strategies;

import static org.ssldev.api.utils.BinaryUtil.intToBytes;

import org.ssldev.api.consumption.SslBuffer;
import org.ssldev.core.utils.Logger;

/**
 * buffer consumption strategy for either a 4 bytes int or 8 bytes long primitives
 */
public class LongOrIntConsumeStrategy extends ConsumeStrategy<Number> {
	@Override
	public void consume(SslBuffer buf) {
		this.bytes = new SslBuffer(buf);
		// length
		length = buf.size();
		Logger.finest(this, "length " + length);

		// value
		if(length == 4)
			this.data = buf.readInt4();
		else if(length == 8)
			this.data = buf.readLong8();
		else
			throw new IllegalStateException("expected int/long length to be 4 or 8. actual is "+length+".  prob wrong consume strategy");
	}

	@Override
	public Number defaultValue() {
		return -1;
	}

	@Override
	public void setData(Number val) {
		bytes.clear();
		bytes.add(intToBytes((int)val));
		data = val;
	}
}