package org.ssldev.api.services;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.ssldev.api.consumption.SslBuffer;
import org.ssldev.api.consumption.SslByteConsumer;
import org.ssldev.api.messages.CrateBytesConsumedMessage;
import org.ssldev.api.messages.CrateConvertRequestMessage;
import org.ssldev.api.messages.CrateConvertResponseMessage;
import org.ssldev.api.messages.CrateConvertedMessage;
import org.ssldev.api.messages.CratesBytesConsumedMessage;
import org.ssldev.api.messages.CratesConvertRequestMessage;
import org.ssldev.api.messages.CratesConvertedMessage;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;
import org.ssldev.core.utils.Logger;

/**
 * converts serato crate files into POJOS. <p>
 * 
 * Consumes: 
 * <ul>
 * <li>{@link CrateConvertRequestMessage}</li>
 * <li>{@link CratesConvertRequestMessage}</li>
 * <li>{@link CrateConvertedMessage}</li>
 * <li>{@link CratesConvertedMessage}</li>
 * </ul>
 * Produces:
 * <ul>
 * <li>{@link CrateBytesConsumedMessage}</li>
 * <li>{@link CratesBytesConsumedMessage}</li>
 * <li>{@link CrateConvertResponseMessage}</li>
 * </ul>
 */
public class CrateConvertService extends Service{

	public CrateConvertService(EventHub hub) {
		super(hub);
	}
	
	@Override
		public void init() {
			super.init();
			hub.register(CrateConvertRequestMessage.class, this::convert);
			hub.register(CratesConvertRequestMessage.class, this::convert);
			hub.register(CrateConvertedMessage.class, this::convert);
			hub.register(CratesConvertedMessage.class, this::convert);
		}

	private void convert(CrateConvertRequestMessage msg) 
	{
		Logger.trace(this.getClass(), "received: "+msg);
		
		try {
			// converts the crate file into bytes that are in turn consumed by the byte consumer
			SslByteConsumer byteConsumer = new SslByteConsumer(new SslBuffer(msg.seratoCrateFile));
			// publish out consumption
			hub.add(new CrateBytesConsumedMessage(byteConsumer, msg.seratoCrateFile, msg.crateName));
		} catch (FileNotFoundException e) {
			Logger.error(this, "encountered exception while attempting convert crate to bytes:\n"+msg,e);
		}
	}
	
	
	// convert multiple crates
	private void convert(CratesConvertRequestMessage msg) {
		Logger.trace(this.getClass(), "received: "+msg);
		
		// converts the crate file into bytes that are in turn consumed by the byte consumer
		List<SslByteConsumer> crateBytes = msg.crates.stream()
													 .map(f -> new SslByteConsumer(createBuffer(f)))
													 .collect(toList());

		// publish out consumption
		hub.add(new CratesBytesConsumedMessage(msg.parentCrateFile, msg.parentCrateName, crateBytes));
		
	}
	
	private SslBuffer createBuffer(File f) {
		try {
			return new SslBuffer(f);
		} catch (FileNotFoundException e) {
			Logger.error(this, "encountered exception while attempting convert crate to bytes:\n"+f,e);
			return new SslBuffer();
		}
	}
	
	private void convert(CrateConvertedMessage msg) {
		Logger.trace(this.getClass(), "got "+msg);
		
		String crateName = msg.crateFile.getName().replaceAll(".crate", "");
		
		List<String> displayedColumns = 
		msg.displayedColumns.stream()
			.map(tvcn -> tvcn.getData())
			.collect(toList());
		
		List<String> crateTracksPaths = 
		msg.ptrks.stream()
			.map(ptrk -> toFilePath((String)ptrk.getData()))
			.collect(toList());
			
		
		this.hub.add(new CrateConvertResponseMessage(msg.crateFile, crateName, displayedColumns, crateTracksPaths));
	}
	private void convert(CratesConvertedMessage msg) {
		Logger.trace(this.getClass(), "got "+msg);
		
		String crateName = msg.parentCrateFile.getName().replaceAll(".crate", "");
		
		List<String> displayedColumns = 
				msg.parentdisplayColumns.stream()
				.map(tvcn -> tvcn.getData())
				.collect(toList());
		
		List<String> crateTracksPaths = 
				msg.ptrks.stream()
				.map(ptrk -> toFilePath((String)ptrk.getData()))
				.collect(toList());
		
		
		this.hub.add(new CrateConvertResponseMessage(msg.parentCrateFile, crateName, displayedColumns, crateTracksPaths));
	}

	
	public static String toFilePath(String p) {
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		sb.append(p.replaceAll(" ", "\\ ").trim());
		return sb.toString();
	}

}
