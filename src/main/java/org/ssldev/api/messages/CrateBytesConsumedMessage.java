package org.ssldev.api.messages;

import java.io.File;

import org.ssldev.api.consumption.ByteConsumerIF;
import org.ssldev.core.messages.Message;
/**
 * Bytes consumed and ready for access notification.  
 */
public class CrateBytesConsumedMessage extends Message {
	public final ByteConsumerIF consumer;
	public final File crateFile;
	public final String crateName;
	
	public CrateBytesConsumedMessage(ByteConsumerIF u, File crateFile, String crateName) {
		consumer = u;
		this.crateFile = crateFile;
		this.crateName = crateName;
	}
	
	@Override
	public String toString() {
		return super.toString() + consumer.getId() + " read in.";
	}
}
