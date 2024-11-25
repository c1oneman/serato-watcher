package org.ssldev.api.consumption;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.ssldev.core.utils.Logger;

public class SslIntBuffer {
	
	private IntBuffer buf;
	
	private ByteBuffer lbuf = ByteBuffer.allocate(8);

	public SslIntBuffer(File inputFile) {
		// add all bytes into buffer
		try (FileInputStream fs = new FileInputStream(inputFile)){
			buf = IntBuffer.allocate(fs.available());
			int b;
			while ((b = fs.read()) >= 0) {
				buf.put(b);
			}
		} catch (IOException e) {
			Logger.error(this, "encountered exception while attempting to convert ["+inputFile+"] to buffer",e);
		}
	}
	
	public SslIntBuffer(SslIntBuffer other) {
		buf = IntBuffer.allocate(other.size());
		buf.put(other.buf);
	}
	
	public boolean isEmpty() { return  buf.remaining() == buf.capacity(); }
	public int size() {return buf.position(); }
	public int poll() {return buf.get(); }
	
	public int readInt() {
		return buf.get();
	}
	
//	public long readLong() {
//		return BinaryUtils.readLong(buf);
//	}
	
	public static short getUnsignedByte(ByteBuffer bb) {
		return ((short) (bb.get() & 0xff));
	}

	public static void putUnsignedByte(ByteBuffer bb, int value) {
		bb.put((byte) (value & 0xff));
	}

	public static short getUnsignedByte(ByteBuffer bb, int position) {
		return ((short) (bb.get(position) & (short) 0xff));
	}

	public static void putUnsignedByte(ByteBuffer bb, int position, int value) {
		bb.put(position, (byte) (value & 0xff));
	}

}
