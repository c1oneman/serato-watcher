package org.ssldev.api.utils;

import static java.util.stream.Collectors.toList;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ssldev.core.utils.Logger;
import org.ssldev.core.utils.StringUtils;

/**
 * utilty to extract ascii from binary data
 */
public class BinaryUtil {
	/** index of byte read in*/
	private static int index;
	

	public static List<Byte> asList(byte[] bytes){
		return Arrays.asList(toByteArray(bytes));
	}
	
	
	public static List<Integer> stringToBytes(String s) {
		return s.chars().boxed().collect(toList());
	}
	public static List<Integer> idToBytes(String id) {
		List<Integer> res = id.chars().mapToObj(Integer::valueOf)
		.collect(toList());
		
		return res;
	}
	
	public static void main(String[] args) {
		//0, 0, 2, 201
		//00000000 00000000 00000010 11001001
		//00000000 00000000 00000010 11001001
		// 1 2 4 8 16 32 64 128
		
		
		
//		Integer v = Integer.valueOf(713);
//		
//		String binaryString = Integer.toBinaryString(713);
//		
//		String a = "00000010";
//		String a2 = "11001001";
//		
//		int b1 = Integer.parseInt("10",2);
//		int b2 = Integer.parseInt("11001001",2);
//		
//		ByteBuffer b = ByteBuffer.allocate(Integer.BYTES);
		
		List<Integer> res = intToBytes(64);


		System.out.println(res);
	}
	
	
	public static List<Integer> intToBytes(int val){
		
		LinkedList<Integer> res = new LinkedList<>();
		
		if(val <= 255) {
			res.add(0);
			res.add(0);
			res.add(0);
			res.add(val);
			return res;
		}

		
		//0, 0, 2, 201   // 10 11001001
		String str = Integer.toBinaryString(val);
		char[] binaryForm = str.toCharArray();
		int len = binaryForm.length;

		while (str.length() > 0) {
			
			if(str.length() < 8) {
				res.add(Integer.parseInt( str , 2));
				break;
			}
			else {
				String c = str.substring(str.length() - 8);
				res.add(Integer.parseInt( c , 2));
				str = str.substring(0, str.length() - 8);
			}
		}
		
		
		while(res.size() < 4) {
			res.add(0);
		}

		List<Integer> reversed = reversed(res);
		
		return reversed;
		
	}
	
	public static List<Integer> intToBytes2(int i){
		IntBuffer buf = IntBuffer.allocate(Integer.BYTES);
		buf.put(i);
//		int[] reversedArray = 
//				IntStream.rangeClosed(1, a.length).map(ii -> a[a.length-ii]).toArray();
		
		return reversed(Arrays.stream(buf.array())
					 .boxed()
					 .collect(toList()));
	}
	
	private static List<Integer> reversed(List<Integer> listToReverse) {
		Collections.reverse(listToReverse);
		
//		Stack<Integer> s = new Stack<>();
//		s.addAll(listToReverse);
//		
//		listToReverse.clear();
//		
//		while(!s.isEmpty()) {
//			listToReverse.add(s.pop());
//		}
		
		return listToReverse;
	}


	static IntStream revRange(int from, int to) {
	    return IntStream.range(from, to).map(i -> to - i + from - 1);
	}
	
	public static Byte[] toByteArray(byte[] bytes) {
		Byte[] byteObjects = new Byte[bytes.length];

		int i=0;    
		for(byte b: bytes)
		   byteObjects[i++] = Byte.valueOf(b); 
		
		return byteObjects;
	}
	public static byte[] toByteArray(Byte[] bytesObjects) {
		byte[] bytes = new byte[bytesObjects.length];
		
		int i=0;    
		for(Byte b: bytesObjects)
			bytes[i++] = b.byteValue();  
		
		return bytes;
	}
	
	public static byte[] toByteArray(List<Byte> byteObjects) {
		byte[] bytes = new byte[byteObjects.size()];
		
		int i= 0;
		for(Byte b: bytes) {
			bytes[i++] = b.byteValue();
		}
		
		return bytes;
	}
	
	public static byte[] toByte(Object o) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		objStream.writeObject(o);

		

		return byteStream.toByteArray();
	}
	
	public static String read(FileInputStream fis, int i) throws IOException {
		StringBuilder sb = new StringBuilder();
		while(i-- > 0) {
			int b = fis.read();
			char c = toChar(b);
			
			if(Logger.isDebugEnabled()) {
				logFinest(index++ + ": " + " " + c + "["+b+"]");
			}
			
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * reads a string of size i from the buffer.  does NOT append '0's. 
	 * @param bytes to read from
	 * @param i size of string to read
	 * @return string read
	 */
	public static String readString(Queue<Integer> bytes, int i) {
		if(i > bytes.size()) {
			Logger.error("BinaryUtil", 
					"asked to readString "+i+" bytes past current buffer size of "+bytes.size());
			throw new IllegalArgumentException("cannot ff buffer by " + i);
		}
		StringBuilder sb = new StringBuilder();
		while(i-- > 0) {
			int b = bytes.poll();
			char c = toChar(b);

			if(Logger.isDebugEnabled()) {
				logFinest(index++ + ": " + " " + c + "["+b+"]");
			}
			
			if(b!=0)sb.append(c);
		}
		
		return sb.toString();
	}

	/**
	 * reads a string of size i from buffer.  appends '0''s to the string.  should not be used
	 * for field like string values 
	 * @param bytes to read from
	 * @param i number of bytes to read
	 * @return string read 
	 */
	public static String readRawString(Queue<Integer> bytes, int i) {
		if(i > bytes.size()) {
			Logger.error("BinaryUtil", 
					"asked to read "+i+" bytes past current buffer size of "+bytes.size());
			throw new IllegalArgumentException("cannot ff buffer by " + i);
		}
		
		StringBuilder sb = new StringBuilder();
		while(i-- > 0) {
			int b = bytes.poll();
			char c = toChar(b);
			if(Logger.isDebugEnabled()) {
				logFinest(index++ + ": " + " " + c + "["+b+"]");
			}
			if(b==0)sb.append(0);
			else sb.append(c);
		}
		return sb.toString();
	}

	public static int readInt(Queue<Integer> bytes, int i) {
		if(i > bytes.size()) {
			Logger.error("BinaryUtil", 
					"asked to readInt "+i+" bytes past current buffer size of "+bytes.size());
			throw new IllegalArgumentException("cannot ff buffer by " + i);
		}
		if(i != 4) {
			//TODO refactor method and take away i parameter
			throw new IllegalArgumentException("can only read 4 ints, not " + i);
		}

		byte[] bs = new byte[i];
		for(int ii=0; ii< i ; ii++) {
			byte b = bytes.poll().byteValue();
			bs[ii] = b;
			if(Logger.isDebugEnabled()) {
				logFinest(index++ + ": " + " " + toChar(bs[ii]) + "["+bs[ii]+"]");
			}
		}
		ByteBuffer wrap = ByteBuffer.wrap(bs);
		int ans = wrap.getInt();
		return ans;
	}
	
	
//	public static void main(String[] args) throws IOException {
//		ByteArrayOutputStream buf = new ByteArrayOutputStream();
//		
//		
//		writeInt(5, buf);
//	}
	
	public static int writeInt(int i, ByteArrayOutputStream buf) throws IOException {
		
		String id = "tart";
		String data = "NWA";
		int len_id = id.length() * 4;
		int len_length = 4;
		int len_d = data.length() * 4;
		
		ByteBuffer b = ByteBuffer.allocate(len_d + len_length +  len_id);
		
		for(char c : id.toCharArray()) {
			b.putChar(c);
		}
		b.putInt(len_d);
		for(char c : data.toCharArray()) {
			b.putChar(c);
		}
		
		
		ObjectOutputStream ost = new ObjectOutputStream(buf);
		
		ost.writeChar(i);

		byte[] arr = buf.toByteArray();
		ByteBuffer wrap = ByteBuffer.wrap(arr);
		
		return 0;
	}
	
	public static long readLong(Queue<Integer> bytes) {
		if(8 > bytes.size()) {
			Logger.error("BinaryUtil", 
					"asked to readInt "+8+" bytes past current buffer size of "+bytes.size());
			throw new IllegalArgumentException("cannot ff buffer by " + 8);
		}
		
		byte[] bs = new byte[8];
		for(int ii=0; ii< 8 ; ii++) {
			byte b = bytes.poll().byteValue();
			bs[ii] = b;
			if(Logger.isDebugEnabled()) {
				logFinest(index++ + ": " + " " + toChar(bs[ii]) + "["+bs[ii]+"]");
			}
		}
		ByteBuffer wrap = ByteBuffer.wrap(bs);
		long ans = wrap.getLong();
		return ans;
	}
	
	

	public static void fastforward(Queue<Integer> buf, int i) {
		if(i > buf.size()) {
			Logger.error("BinaryUtil", 
					"asked to fastforward the buffer "+i+" past its current size of "+buf.size());
			throw new IllegalArgumentException("cannot ff buffer by " + i);
		}
		logFinest("fastforwarding the buffer by " +i+":");
		while(i-- >0) { 
			int b = buf.poll();
			char c = toChar(b);
			if(Logger.isDebugEnabled()) {
				logFinest(index++ + ": " + " " + c + "["+b+"]");
			}
		}
	}

	private static void logFinest(String s) {
		Logger.finest(BinaryUtil.class, s);
	}

	private static char toChar(int c) {
		return (char)c;
	}

	/**
	 * reads a string til encountering an empty string ("0000")
	 * @param buf to read from
	 * @return string read
	 */
	public static String readTilEmpty(Queue<Integer> buf) {
		StringBuilder ret = new StringBuilder();
		String b;
		do {
			b = readString(buf,4);
			ret.append(b);
			
		} while(!b.isEmpty());
		
		return ret.toString();
	}

	private static String peekString(Queue<Integer> buf, int i) {
		if(i > buf.size()) throw new IllegalArgumentException("cannot peek string of length " +i+" when buffer is size " +buf.size());
		
		StringBuilder sb = new StringBuilder(); Iterator<Integer> itr = buf.iterator();
		while(i-- > 0 && itr.hasNext()) {
			sb.append(itr.next());
		}
		return sb.toString();
	}

	private static void copy(Queue<Integer> from, Queue<Integer> to, int numbytes) {
		Iterator<Integer> itr = from.iterator();
		while(numbytes-- > 0) {
			if(itr.hasNext()) to.add(itr.next());
		}
	}

	private static void move(Queue<Integer> from, Queue<Integer> to, int numbytes) {
		while(numbytes-- > 0) {
			to.add(from.poll());
		}
	}

	public static int readOneByte(Queue<Integer> bytes) {
		if(bytes.isEmpty()) throw new IllegalArgumentException("buffer is empty");
		int b = bytes.poll();
		if(Logger.isDebugEnabled()) {
			logFinest(index++ + ": " + " " + toChar(b) + "["+b+"]");
		}
		return b;
	}

	public static int readIntLength1(Queue<Integer> buf) {
		return buf.poll();
	}


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
