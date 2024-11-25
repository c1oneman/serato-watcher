package org.ssldev.api.consumption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.ssldev.api.utils.BinaryUtil;
import org.ssldev.core.utils.Logger;

public class SslBuffer {
	
	private LinkedList<Integer> buf = new LinkedList<Integer>();

	public SslBuffer() {
		// empty
	}
	
//	public SslBuffer(Queue<Integer> ret) {
//		buf.addAll(ret);
//	}
	
	public SslBuffer(File inputFile) throws FileNotFoundException {

		// add all bytes into buffer
		try (FileInputStream fs = new FileInputStream(inputFile)){
			int b;
			while ((b = fs.read()) >= 0) {
				this.add(b);
			}
		} catch (IOException e) {
			Logger.error(this, "encountered exception while attempting to convert ["+inputFile+"] to buffer:");
			e.printStackTrace();
		}
	}
	
	public void write(File outputFile) {
		
		Logger.info(this, "writing buffer of size ["+buf.size()+"] to file: ["+outputFile+"]");
		
		try (FileOutputStream fo = new FileOutputStream(outputFile)){
			
			if(!outputFile.exists()) {
				Logger.info(this, "creating output file: ["+outputFile+"]");
				outputFile.createNewFile();
			}
			
			Queue<Integer> bufCopy = new LinkedList<>(buf);
			while(!bufCopy.isEmpty()) {
				fo.write(bufCopy.remove());
			}
			
			fo.flush();
			fo.close();
			
		} catch (IOException e) {
			Logger.error(this, "encountered exception while attempting to write ["+outputFile+"] to disk:");
			e.printStackTrace();
		}
	}
	
	public SslBuffer(SslBuffer buf) {
		this.buf.addAll(buf.buf);
	}

	public SslBuffer(Collection<Integer> bs) {
		buf.addAll(bs);
	}

	public boolean isEmpty() {return buf.isEmpty();}
	public int size() {return buf.size();}
	public int poll() {return BinaryUtil.readOneByte(buf);}
	
	public int readInt4() {
		return BinaryUtil.readInt(buf, 4);
	}
	public long readLong8() {
		return BinaryUtil.readLong(buf);
	}
	public String readRawString(int len) {
		return BinaryUtil.readRawString(buf, len);
	}
	
	public SslBuffer remove(int length) {
		if(length > buf.size()) throw new IllegalArgumentException("cannot remove "+length+". buffer size is only " +buf.size());
		
		Queue<Integer> ret = new LinkedList<>();
		while(length-- > 0) ret.add(BinaryUtil.readOneByte(buf)); 
		
		return new SslBuffer(ret);
	}
	public String readString(int length) {
		return BinaryUtil.readString(buf, length);
	}

	public void fastforward(int len) {
		BinaryUtil.fastforward(buf, len);
	}
	
	public void add(SslBuffer otherBuffer) {
		buf.addAll(otherBuffer.buf);
	}
	
	public void add(int b) {
		buf.add(b);
	}
	public void add(List<Integer> otherBytes) {
		buf.addAll(otherBytes);
	}
	
	public void clear() {
		buf.clear();
	}
	
	public SslBuffer copy() {
		return new SslBuffer(new LinkedList<Integer>(buf));
	}
	
	public void copy(SslBuffer otherBuffer, int numElementsToCopy) {
		if(numElementsToCopy > otherBuffer.size()) {
			throw new IllegalArgumentException("asked to copy ["
					+numElementsToCopy+"] elements but size of buffer is only ["+otherBuffer.size()+"]");
		}
		
		for(int i=0; i < numElementsToCopy; i++) {
			buf.add(otherBuffer.buf.get(i));
		}
	}

	/**
	 * @return underlying live buffer. 
	 */
	public Queue<Integer> getBytes() {
		return buf;
	}

	public byte[] toBytes() {
		
		Byte[] asBytes = buf.stream()
							.map(integer -> integer.byteValue())
							.toArray(Byte[]::new);
		
		return BinaryUtil.toByteArray(asBytes);
	}
	
	public List<Integer> asList() {
		return new LinkedList<>(buf);
	}
	
	@Override
	public String toString() {
		return buf.size() + "\n" +buf.toString();
	}


}
