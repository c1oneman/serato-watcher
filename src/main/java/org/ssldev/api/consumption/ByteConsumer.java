package org.ssldev.api.consumption;

import java.util.List;

import org.ssldev.api.consumption.strategies.ConsumeStrategy;
import org.ssldev.api.utils.BinaryUtil;
import org.ssldev.core.utils.Logger;

public abstract class ByteConsumer implements ByteConsumerIF{
	/** register ID of this consumer. buffer block to consume is assigned by this ID */ 
	protected String id;
	
	/** name of this consumer*/
	protected String name;
	
	/** how the buffer should be consumed */
	protected ConsumeStrategy consume;
	
	/** data consumed from buffer (could be a sub-buffer)*/
	protected Object data;
	
	/** register for any sub-consumers */
	private BufferConsumerFactory register = new BufferConsumerFactory();

	@Override
	public String getId() {
		return null == id? "" : id;
	}

	@Override
	public String getName() {
		return null == name? "" : id;
	}
	

	public void consume(SslBuffer bytes) {
		Logger.finest(this, " consuming ID <" + id+ ">.  bytes size: " + bytes.size());
		
		try {
			consume.consume(bytes);
			data = consume.getData();
		} catch (Throwable t) {
			Logger.error(this, "encountered unmarshalling error",t);
			data = consume.defaultValue();  // keep going
		}
	}
	
	@Override
	public int size() {
		return consume.length;
	}
	
	public void register(ByteConsumerIF b) {
		register.register(b.getId(), b);
	} 

	public abstract ByteConsumerIF getInstance();

	public boolean containsConsumer(BufferConsumerKey key) {
		return register.isRegistered(key);
	}
	
	public ByteConsumerIF buildConsumer(BufferConsumerKey key) {
		return register.build(key);
	}
	
	public ByteConsumerIF buildConsumer(String key) {
		return register.build(key);
	}
	public ByteConsumerIF buildConsumer(IntString key) {
		return register.build(key);
	}
	
	//TODO add generics
	public void setData(Object val) {
		consume.setData(val);
		data = val;
	}
	
	/**
	 * get data that was consumed by the consumers ID
	 * @param _id of the consumer
	 * @return data consumed by ID or null if consumer not found
	 */
	@SuppressWarnings("unchecked")
	//TODO deprecate.  
	protected ByteConsumerIF getData(String _id) {
		if(data instanceof List<?>) {
			for(ByteConsumerIF b: ((List<ByteConsumerIF>)data)) {
				if(b.getId().equals(_id)) return b;
			}
		}
		return null;
	}
//	@SuppressWarnings("unchecked")
//	@Override
//	public void removeAll(Class<Utpc> type) {
//		if(data instanceof Collection) {
//			Collection<ByteConsumerIF> col = ((Collection<ByteConsumerIF>)data);
//			col.forEach(c -> c.removeAll(type));
//			
//		}
//		else {
//			if(type.isAssignableFrom(this.getClass())) {
//				data = null;
//				consume.clear();
//			}
//		}
//	}
	
	
	public List<Integer> idToBytes() {
		return BinaryUtil.stringToBytes(id);
	}
	
	@Override
	public List<Integer> toBytes() {
		return consume.toBytes();
	}
	
	
	@Override
	public String toString() {
		return "["+id+"]: ["+consume.getData()+"]";
	}

	public boolean isRegistered(IntString key) {
		return register.isRegistered(key);
	}

}
