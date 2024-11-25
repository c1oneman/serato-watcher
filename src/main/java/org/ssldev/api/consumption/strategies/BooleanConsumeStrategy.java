package org.ssldev.api.consumption.strategies;

import org.ssldev.api.consumption.SslBuffer;
import org.ssldev.core.utils.Logger;

public class BooleanConsumeStrategy extends ConsumeStrategy<Boolean> {

	@Override
	public void consume(SslBuffer buf) {
		this.bytes = new SslBuffer(buf);
		// length
		length = buf.size();
		Logger.finest(this, "length " + length);
		if(length != 1) throw new IllegalStateException("expected bool length to be 1. actual is "+length+".  prob wrong consume strategy");

		// value
		this.data = buf.readRawString(1).equals("1") ? true : false;
	}

	@Override
	public Boolean defaultValue() {
		return false;
	}

	@Override
	public void setData(Boolean val) {
		bytes.clear();
		bytes.add( data == true ? 1 : 0);
	}

}
