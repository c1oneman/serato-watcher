package org.ssldev.api.consumption;


import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BufferConsumptionKeyTest {
	private BufferConsumerKey key;
	private IntString akey;

	@BeforeEach
	public void setUp() throws Exception {
		key = new BufferConsumerKey();
	}

	@Test
	public void testIntKey() {
		key.appendToKey(0);
		key.appendToKey(0);
		key.appendToKey(0);
		key.appendToKey(22);
		
		assertEquals(4,key.getLength());
		
		assertEquals("22",key.getKeyAsInteger());
	}
	@Test
	public void testStringKey() {
		key.appendToKey('v');
		key.appendToKey('r');
		key.appendToKey('s');
		key.appendToKey('n');
		
		assertEquals(4,key.getLength());
		
		assertEquals("vrsn",key.getKeyAsString());
	}

	@Test
	public void testIntStringIntKey() {
		SslBuffer sb = new SslBuffer();
		sb.add(0);
		sb.add(0);
		sb.add(0);
		sb.add(22);
		akey = new IntString(sb);
		
		assertEquals("22",akey.getIntVal());  
	}
	@Test
	public void testIntStringStringKey() {
		SslBuffer sb = new SslBuffer();
		sb.add((int)'v');
		sb.add((int)'r');
		sb.add((int)'s');
		sb.add((int)'n');
		akey = new IntString(sb);
		
		assertEquals("vrsn",akey.getStringVal());
	}


}
