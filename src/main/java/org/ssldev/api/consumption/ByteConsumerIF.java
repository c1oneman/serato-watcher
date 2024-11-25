package org.ssldev.api.consumption;

import java.util.List;

/**
 * an object that knows how to consume bytes 
 */
//TODO add generics
public interface ByteConsumerIF {

	String getId();

	String getName();

	void consume(SslBuffer bytes);

	ByteConsumerIF getInstance();

	Object getData();
	
	void setData(Object val);
	
	List<Integer> toBytes();

	int size();
}
