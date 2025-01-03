package org.ssldev.api.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.ssldev.api.chunks.Otrk;
import org.ssldev.api.chunks.Pfil;
import org.ssldev.api.chunks.Utpc;
import org.ssldev.api.consumption.SslBuffer;
import org.ssldev.api.consumption.SslByteConsumer;
import org.ssldev.api.consumption.SslIntBuffer;

public class BinaryIntArrayUtilTest {

	String dbPath = "src/test/resources/org/ssldev/api/1_song_db";
	String emptyDbPath = "src/test/resources/org/ssldev/api/empty_db";
	
	@Before
	public void setUp() throws Exception {
	}

	@Test public void assertTestFileExists() {
		File f = new File(dbPath);
		assertTrue(f.exists());
	}
	
	@Test public void loadFile() {
		SslIntBuffer buf = new SslIntBuffer(new File(dbPath));
		
		assertFalse(buf.isEmpty());
	}
	@Test public void loadFileIntoSslBuffer() throws FileNotFoundException {
		SslBuffer buf = new SslBuffer(new File(dbPath));
		SslByteConsumer consumer = new SslByteConsumer(buf);
		List<Integer> consumerAsBytes = consumer.toBytes();
		SslByteConsumer consumerCopy = new SslByteConsumer(new SslBuffer(consumerAsBytes));
		assertEquals(consumerAsBytes, consumerCopy.toBytes());
	} 

	File l = new File("/Users/elad/Music/_Serato_/database V2");
	@Ignore
	@Test public void overwriteCountField() throws FileNotFoundException {
		File dbFile = l;
//		File dbFile = new File(dbPath);
		SslBuffer buf = new SslBuffer(dbFile );
		SslByteConsumer consumer = new SslByteConsumer(buf);
		
		List<Otrk> trks =  consumer.getAll(Otrk.class);
		

		// find track whose path matches the following
		String songFilePath = "Users/elad/Desktop/test/boyz.mp3"; 
		Otrk trk = trks.stream()
		.filter(t -> t.get(Pfil.class).map(pfil -> pfil.getData().equals(songFilePath)).orElse(false))
		.findFirst().orElse(null)
//		.ifPresent(o -> o.set(Utpc.class, "4"));
		;

		// overwrite count field
		trk.set(Utpc.class, 256);

		consumer.write(dbFile);
		
		System.out.println("done");
	}
	
	
	
// [118, 114, 115, 110, 0, 0, 0, 64, 0, 50, 0, 46, 0, 48, 0, 47, 0, 83, 0, 101, 0, 114, 0, 97, 0, 116, 0, 111, 0, 32, 0, 83, 0, 99, 0, 114, 0, 97, 0, 116, 0, 99, 0, 104, 0, 32, 0, 76, 0, 73, 0, 86, 0, 69, 0, 32, 0, 68, 0, 97, 0, 116, 0, 97, 0, 98, 0, 97, 0, 115, 0, 101, 111, 116, 114, 107, 0, 0, 2, 201, 116, 116, 121, 112, 0, 0, 0, 6, 0, 109, 0, 112, 0, 51, 112, 102, 105, 108, 0, 0, 0, 118, 0, 85, 0, 115, 0, 101, 0, 114, 0, 115, 0, 47, 0, 101, 0, 108, 0, 97, 0, 100, 0, 47, 0, 68, 0, 101, 0, 115, 0, 107, 0, 116, 0, 111, 0, 112, 0, 47, 0, 116, 0, 101, 0, 115, 0, 116, 0, 47, 0, 48, 0, 49, 0, 32, 0, 66, 0, 111, 0, 121, 0, 122, 0, 32, 0, 105, 0, 110, 0, 32, 0, 116, 0, 104, 0, 101, 0, 32, 0, 72, 0, 111, 0, 111, 0, 100, 0, 32, 0, 40, 0, 68, 0, 105, 0, 114, 0, 116, 0, 121, 0, 41, 0, 40, 0, 49, 0, 50, 0, 41, 0, 46, 0, 109, 0, 112, 0, 51, 116, 115, 110, 103, 0, 0, 0, 56, 0, 66, 0, 111, 0, 121, 0, 122, 0, 32, 0, 105, 0, 110, 0, 32, 0, 116, 0, 104, 0, 101, 0, 32, 0, 72, 0, 111, 0, 111, 0, 100, 0, 32, 0, 40, 0, 68, 0, 105, 0, 114, 0, 116, 0, 121, 0, 41, 0, 40, 0, 49, 0, 50, 0, 41, 116, 97, 114, 116, 0, 0, 0, 6, 0, 78, 0, 87, 0, 65, 116, 97, 108, 98, 0, 0, 0, 44, 0, 83, 0, 116, 0, 114, 0, 97, 0, 105, 0, 103, 0, 104, 0, 116, 0, 32, 0, 79, 0, 117, 0, 116, 0, 116, 0, 97, 0, 32, 0, 67, 0, 111, 0, 109, 0, 112, 0, 116, 0, 111, 0, 110, 116, 103, 101, 110, 0, 0, 0, 48, 0, 72, 0, 105, 0, 112, 0, 32, 0, 72, 0, 111, 0, 112, 0, 32, 0, 47, 0, 32, 0, 56, 0, 48, 0, 39, 0, 115, 0, 32, 0, 79, 0, 108, 0, 100, 0, 115, 0, 99, 0, 104, 0, 111, 0, 111, 0, 108, 116, 108, 101, 110, 0, 0, 0, 16, 0, 48, 0, 52, 0, 58, 0, 48, 0, 52, 0, 46, 0, 51, 0, 48, 116, 98, 105, 116, 0, 0, 0, 18, 0, 50, 0, 50, 0, 52, 0, 46, 0, 48, 0, 107, 0, 98, 0, 112, 0, 115, 116, 115, 109, 112, 0, 0, 0, 10, 0, 52, 0, 52, 0, 46, 0, 49, 0, 107, 116, 98, 112, 109, 0, 0, 0, 10, 0, 56, 0, 55, 0, 46, 0, 56, 0, 49, 116, 99, 111, 109, 0, 0, 0, 4, 0, 54, 0, 65, 116, 99, 109, 112, 0, 0, 0, 54, 0, 46, 0, 56, 0, 48, 0, 115, 0, 46, 0, 32, 0, 46, 0, 57, 0, 48, 0, 115, 0, 46, 0, 32, 0, 46, 0, 114, 0, 97, 0, 112, 0, 46, 0, 32, 0, 46, 0, 103, 0, 97, 0, 110, 0, 103, 0, 115, 0, 116, 0, 97, 0, 46, 116, 97, 100, 100, 0, 0, 0, 20, 0, 49, 0, 53, 0, 55, 0, 53, 0, 48, 0, 52, 0, 56, 0, 51, 0, 49, 0, 50, 116, 107, 101, 121, 0, 0, 0, 4, 0, 54, 0, 65, 117, 97, 100, 100, 0, 0, 0, 4, 93, 225, 84, 120, 117, 116, 107, 110, 0, 0, 0, 4, 0, 0, 0, 1, 117, 108, 98, 108, 0, 0, 0, 4, 0, 255, 255, 255, 117, 116, 109, 101, 0, 0, 0, 4, 93, 145, 181, 35, 117, 116, 112, 99, 0, 0, 0, 4, 0, 0, 0, 2, 115, 98, 97, 118, 0, 0, 0, 2, 2, 1, 98, 104, 114, 116, 0, 0, 0, 1, 1, 98, 109, 105, 115, 0, 0, 0, 1, 0, 98, 112, 108, 121, 0, 0, 0, 1, 1, 98, 108, 111, 112, 0, 0, 0, 1, 0, 98, 105, 116, 117, 0, 0, 0, 1, 0, 98, 111, 118, 99, 0, 0, 0, 1, 1, 98, 99, 114, 116, 0, 0, 0, 1, 0, 98, 105, 114, 111, 0, 0, 0, 1, 0, 98, 119, 108, 98, 0, 0, 0, 1, 0, 98, 119, 108, 108, 0, 0, 0, 1, 0, 98, 117, 110, 115, 0, 0, 0, 1, 0, 98, 98, 103, 108, 0, 0, 0, 1, 0, 98, 107, 114, 107, 0, 0, 0, 1, 0]
// [118, 114, 115, 110, 0, 0, 0, 64, 0, 50, 0, 46, 0, 48, 0, 47, 0, 83, 0, 101, 0, 114, 0, 97, 0, 116, 0, 111, 0, 32, 0, 83, 0, 99, 0, 114, 0, 97, 0, 116, 0, 99, 0, 104, 0, 32, 0, 76, 0, 73, 0, 86, 0, 69, 0, 32, 0, 68, 0, 97, 0, 116, 0, 97, 0, 98, 0, 97, 0, 115, 0, 101, 111, 116, 114, 107, 0, 0, 0, 713, 116, 116, 121, 112, 0, 0, 0, 6, 0, 109, 0, 112, 0, 51, 112, 102, 105, 108, 0, 0, 0, 118, 0, 85, 0, 115, 0, 101, 0, 114, 0, 115, 0, 47, 0, 101, 0, 108, 0, 97, 0, 100, 0, 47, 0, 68, 0, 101, 0, 115, 0, 107, 0, 116, 0, 111, 0, 112, 0, 47, 0, 116, 0, 101, 0, 115, 0, 116, 0, 47, 0, 48, 0, 49, 0, 32, 0, 66, 0, 111, 0, 121, 0, 122, 0, 32, 0, 105, 0, 110, 0, 32, 0, 116, 0, 104, 0, 101, 0, 32, 0, 72, 0, 111, 0, 111, 0, 100, 0, 32, 0, 40, 0, 68, 0, 105, 0, 114, 0, 116, 0, 121, 0, 41, 0, 40, 0, 49, 0, 50, 0, 41, 0, 46, 0, 109, 0, 112, 0, 51, 116, 115, 110, 103, 0, 0, 0, 56, 0, 66, 0, 111, 0, 121, 0, 122, 0, 32, 0, 105, 0, 110, 0, 32, 0, 116, 0, 104, 0, 101, 0, 32, 0, 72, 0, 111, 0, 111, 0, 100, 0, 32, 0, 40, 0, 68, 0, 105, 0, 114, 0, 116, 0, 121, 0, 41, 0, 40, 0, 49, 0, 50, 0, 41, 116, 97, 114, 116, 0, 0, 0, 6, 0, 78, 0, 87, 0, 65, 116, 97, 108, 98, 0, 0, 0, 44, 0, 83, 0, 116, 0, 114, 0, 97, 0, 105, 0, 103, 0, 104, 0, 116, 0, 32, 0, 79, 0, 117, 0, 116, 0, 116, 0, 97, 0, 32, 0, 67, 0, 111, 0, 109, 0, 112, 0, 116, 0, 111, 0, 110, 116, 103, 101, 110, 0, 0, 0, 48, 0, 72, 0, 105, 0, 112, 0, 32, 0, 72, 0, 111, 0, 112, 0, 32, 0, 47, 0, 32, 0, 56, 0, 48, 0, 39, 0, 115, 0, 32, 0, 79, 0, 108, 0, 100, 0, 115, 0, 99, 0, 104, 0, 111, 0, 111, 0, 108, 116, 108, 101, 110, 0, 0, 0, 16, 0, 48, 0, 52, 0, 58, 0, 48, 0, 52, 0, 46, 0, 51, 0, 48, 116, 98, 105, 116, 0, 0, 0, 18, 0, 50, 0, 50, 0, 52, 0, 46, 0, 48, 0, 107, 0, 98, 0, 112, 0, 115, 116, 115, 109, 112, 0, 0, 0, 10, 0, 52, 0, 52, 0, 46, 0, 49, 0, 107, 116, 98, 112, 109, 0, 0, 0, 10, 0, 56, 0, 55, 0, 46, 0, 56, 0, 49, 116, 99, 111, 109, 0, 0, 0, 4, 0, 54, 0, 65, 116, 99, 109, 112, 0, 0, 0, 54, 0, 46, 0, 56, 0, 48, 0, 115, 0, 46, 0, 32, 0, 46, 0, 57, 0, 48, 0, 115, 0, 46, 0, 32, 0, 46, 0, 114, 0, 97, 0, 112, 0, 46, 0, 32, 0, 46, 0, 103, 0, 97, 0, 110, 0, 103, 0, 115, 0, 116, 0, 97, 0, 46, 116, 97, 100, 100, 0, 0, 0, 20, 0, 49, 0, 53, 0, 55, 0, 53, 0, 48, 0, 52, 0, 56, 0, 51, 0, 49, 0, 50, 116, 107, 101, 121, 0, 0, 0, 4, 0, 54, 0, 65, 117, 97, 100, 100, 0, 0, 0, 4, 93, 225, 84, 120, 117, 116, 107, 110, 0, 0, 0, 4, 0, 0, 0, 1, 117, 108, 98, 108, 0, 0, 0, 4, 0, 255, 255, 255, 117, 116, 109, 101, 0, 0, 0, 4, 93, 145, 181, 35, 117, 116, 112, 99, 0, 0, 0, 4, 0, 0, 0, 2, 115, 98, 97, 118, 0, 0, 0, 2, 2, 1, 98, 104, 114, 116, 0, 0, 0, 1, 1, 98, 109, 105, 115, 0, 0, 0, 1, 0, 98, 112, 108, 121, 0, 0, 0, 1, 1, 98, 108, 111, 112, 0, 0, 0, 1, 0, 98, 105, 116, 117, 0, 0, 0, 1, 0, 98, 111, 118, 99, 0, 0, 0, 1, 1, 98, 99, 114, 116, 0, 0, 0, 1, 0, 98, 105, 114, 111, 0, 0, 0, 1, 0, 98, 119, 108, 98, 0, 0, 0, 1, 0, 98, 119, 108, 108, 0, 0, 0, 1, 0, 98, 117, 110, 115, 0, 0, 0, 1, 0, 98, 98, 103, 108, 0, 0, 0, 1, 0, 98, 107, 114, 107, 0, 0, 0, 1, 0]
// [118, 114, 115, 110, 0, 0, 0, 64, 0, 50, 0, 46, 0, 48, 0, 47, 0, 83, 0, 101, 0, 114, 0, 97, 0, 116, 0, 111, 0, 32, 0, 83, 0, 99, 0, 114, 0, 97, 0, 116, 0, 99, 0, 104, 0, 32, 0, 76, 0, 73, 0, 86, 0, 69, 0, 32, 0, 68, 0, 97, 0, 116, 0, 97, 0, 98, 0, 97, 0, 115, 0, 101]
// [118, 114, 115, 110, 0, 0, 0, 64, 0, 50, 0, 46, 0, 48, 0, 47, 0, 83, 0, 101, 0, 114, 0, 97, 0, 116, 0, 111, 0, 32, 0, 83, 0, 99, 0, 114, 0, 97, 0, 116, 0, 99, 0, 104, 0, 32, 0, 76, 0, 73, 0, 86, 0, 69, 0, 32, 0, 68, 0, 97, 0, 116, 0, 97, 0, 98, 0, 97, 0, 115, 0, 101, 111, 116, 114, 107, 0, 0, 2, 201, 116, 116, 121, 112, 0, 0, 0, 6, 0, 109, 0, 112, 0, 51, 112, 102, 105, 108, 0, 0, 0, 118, 0, 85, 0, 115, 0, 101, 0, 114, 0, 115, 0, 47, 0, 101, 0, 108, 0, 97, 0, 100, 0, 47, 0, 68, 0, 101, 0, 115, 0, 107, 0, 116, 0, 111, 0, 112, 0, 47, 0, 116, 0, 101, 0, 115, 0, 116, 0, 47, 0, 48, 0, 49, 0, 32, 0, 66, 0, 111, 0, 121, 0, 122, 0, 32, 0, 105, 0, 110, 0, 32, 0, 116, 0, 104, 0, 101, 0, 32, 0, 72, 0, 111, 0, 111, 0, 100, 0, 32, 0, 40, 0, 68, 0, 105, 0, 114, 0, 116, 0, 121, 0, 41, 0, 40, 0, 49, 0, 50, 0, 41, 0, 46, 0, 109, 0, 112, 0, 51, 116, 115, 110, 103, 0, 0, 0, 56, 0, 66, 0, 111, 0, 121, 0, 122, 0, 32, 0, 105, 0, 110, 0, 32, 0, 116, 0, 104, 0, 101, 0, 32, 0, 72, 0, 111, 0, 111, 0, 100, 0, 32, 0, 40, 0, 68, 0, 105, 0, 114, 0, 116, 0, 121, 0, 41, 0, 40, 0, 49, 0, 50, 0, 41, 116, 97, 114, 116, 0, 0, 0, 6, 0, 78, 0, 87, 0, 65, 116, 97, 108, 98, 0, 0, 0, 44, 0, 83, 0, 116, 0, 114, 0, 97, 0, 105, 0, 103, 0, 104, 0, 116, 0, 32, 0, 79, 0, 117, 0, 116, 0, 116, 0, 97, 0, 32, 0, 67, 0, 111, 0, 109, 0, 112, 0, 116, 0, 111, 0, 110, 116, 103, 101, 110, 0, 0, 0, 48, 0, 72, 0, 105, 0, 112, 0, 32, 0, 72, 0, 111, 0, 112, 0, 32, 0, 47, 0, 32, 0, 56, 0, 48, 0, 39, 0, 115, 0, 32, 0, 79, 0, 108, 0, 100, 0, 115, 0, 99, 0, 104, 0, 111, 0, 111, 0, 108, 116, 108, 101, 110, 0, 0, 0, 16, 0, 48, 0, 52, 0, 58, 0, 48, 0, 52, 0, 46, 0, 51, 0, 48, 116, 98, 105, 116, 0, 0, 0, 18, 0, 50, 0, 50, 0, 52, 0, 46, 0, 48, 0, 107, 0, 98, 0, 112, 0, 115, 116, 115, 109, 112, 0, 0, 0, 10, 0, 52, 0, 52, 0, 46, 0, 49, 0, 107, 116, 98, 112, 109, 0, 0, 0, 10, 0, 56, 0, 55, 0, 46, 0, 56, 0, 49, 116, 99, 111, 109, 0, 0, 0, 4, 0, 54, 0, 65, 116, 99, 109, 112, 0, 0, 0, 54, 0, 46, 0, 56, 0, 48, 0, 115, 0, 46, 0, 32, 0, 46, 0, 57, 0, 48, 0, 115, 0, 46, 0, 32, 0, 46, 0, 114, 0, 97, 0, 112, 0, 46, 0, 32, 0, 46, 0, 103, 0, 97, 0, 110, 0, 103, 0, 115, 0, 116, 0, 97, 0, 46, 116, 97, 100, 100, 0, 0, 0, 20, 0, 49, 0, 53, 0, 55, 0, 53, 0, 48, 0, 52, 0, 56, 0, 51, 0, 49, 0, 50, 116, 107, 101, 121, 0, 0, 0, 4, 0, 54, 0, 65, 117, 97, 100, 100, 0, 0, 0, 4, 93, 225, 84, 120, 117, 116, 107, 110, 0, 0, 0, 4, 0, 0, 0, 1, 117, 108, 98, 108, 0, 0, 0, 4, 0, 255, 255, 255, 117, 116, 109, 101, 0, 0, 0, 4, 93, 145, 181, 35, 117, 116, 112, 99, 0, 0, 0, 4, 0, 0, 0, 2, 115, 98, 97, 118, 0, 0, 0, 2, 2, 1, 98, 104, 114, 116, 0, 0, 0, 1, 1, 98, 109, 105, 115, 0, 0, 0, 1, 0, 98, 112, 108, 121, 0, 0, 0, 1, 1, 98, 108, 111, 112, 0, 0, 0, 1, 0, 98, 105, 116, 117, 0, 0, 0, 1, 0, 98, 111, 118, 99, 0, 0, 0, 1, 1, 98, 99, 114, 116, 0, 0, 0, 1, 0, 98, 105, 114, 111, 0, 0, 0, 1, 0, 98, 119, 108, 98, 0, 0, 0, 1, 0, 98, 119, 108, 108, 0, 0, 0, 1, 0, 98, 117, 110, 115, 0, 0, 0, 1, 0, 98, 98, 103, 108, 0, 0, 0, 1, 0, 98, 107, 114, 107, 0, 0, 0, 1, 0]	

	@Test public void readLong() {
		
		IntBuffer ibuf = IntBuffer.allocate(Long.BYTES);
		
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(43);
		buffer.flip();
		while(buffer.hasRemaining()) {
			ibuf.put(buffer.get());
		}
	    
//	    buffer.position(0);
	    
	    
//	    IntBuffer ibuf = toIntBuffer(buffer);
//	    
//	    long val = BinaryUtils.readLong(ibuf);
//	    assertEquals(43, val);
	}
	
	private IntBuffer toIntBuffer(ByteBuffer b) {
		IntBuffer buf = IntBuffer.allocate(b.capacity());
		
//		byte[] array = b.array();
//		for(byte by : array) {
//			System.out.println(by);
//		}
		while(b.hasRemaining()) {
			System.out.println(b.get());
		}
		
//		while(b.hasRemaining()) {
//			byte v = b.get();
//			buf.put(v);
//		}
		return buf;
	}

}
