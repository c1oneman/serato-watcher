package org.ssldev.api.consumption.strategies;

import static java.util.Collections.emptyList;

import java.util.List;

import org.ssldev.api.consumption.SslBuffer;
import org.ssldev.api.utils.BinaryUtil;

/**
 * biz logic that knows how to consume a buffer.
 * @param T type of data to be consumed 
 */
public abstract class ConsumeStrategy <T> {
	public int length; 
	public T data;
	public SslBuffer bytes;

	public abstract void consume(SslBuffer buf);
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
		if(null != data) sb.append("\n").append(data);
		return sb.toString();
	}

	public T getData() {
		return data;
	}
	
	/**
	 * override the consumed data and bytes stored with the given value
	 * @param val to set
	 */
	public abstract void setData(T val);

	/**
	 * returns default value.  called whenever strategy encounters an error
	 * @return default value
	 */
	public abstract T defaultValue();

	
	/**
	 * 
	 * @return the byte (in int form) value of the data object 
	 */
	public List<Integer> toBytes(){
		return null == bytes ? emptyList() : bytes.asList();
	}
	
	public List<Integer> lengthToBytes(){
		return BinaryUtil.intToBytes(length);
	}

	public void clear() {
		bytes.clear();
		length = 0;
		data = defaultValue();
	}

}
