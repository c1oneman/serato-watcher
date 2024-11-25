package org.ssldev.api.consumption;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.List;

import org.ssldev.api.chunks.Oent;
import org.ssldev.api.chunks.Osrt;
import org.ssldev.api.chunks.Otrk;
import org.ssldev.api.chunks.Ovct;
import org.ssldev.api.chunks.Vrsn;
import org.ssldev.api.consumption.strategies.CompoundChunkConsumeStrategy;
import org.ssldev.core.utils.Logger;

/**
 * consumes an SSL byte buffer and breaks it down into chunks.  Chunks should register 
 * with this class in order to process associated bytes.  Assumes only one chunk per 
 * ID.  Dumps data whose ID is not known.
 */
public class SslByteConsumer extends ByteConsumer {
	
	public SslByteConsumer() {
		id = name = "SSL Data Consumer";
		
		constructConsumer();
		
		consume = new CompoundChunkConsumeStrategy(this);
	}
	
	public SslByteConsumer(SslBuffer bytes) {
		this();
		consume.consume(bytes);
		if(!bytes.isEmpty()) {
			Logger.error(this, "expected the SSL Buffer to be empty after consumption. size is actually "+bytes.size());
		}
	}

	private void constructConsumer() {
		register(new Oent());
		register(new Vrsn());
		register(new Osrt());
		register(new Ovct());
		register(new Otrk());
	}

	@Override
	public ByteConsumerIF getInstance() {
		return new SslByteConsumer();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ByteConsumerIF> getData() {
		return (List<ByteConsumerIF>) consume.getData();
	}


	public <T> List<T> getAll(Class<T> type) {

		return getData().stream()
				.filter(type::isInstance)
				.map(type::cast)
				.collect(toList());
	}

	public void write(File outputFile) {
		new SslBuffer(toBytes())
			.write(outputFile);
	}

}
