package org.ssldev.api.messages;

import java.io.File;
import java.util.List;

import org.ssldev.api.consumption.SslByteConsumer;
import org.ssldev.core.messages.Message;

/**
 * serato crates converted into bytes
 */
public class CratesBytesConsumedMessage extends Message {
	public final File parentCrateFile;
	public final String parentCrateName;
	public final List<SslByteConsumer> crateBytes;

	public CratesBytesConsumedMessage(File parentCrateFile, String parentCrateName, List<SslByteConsumer> crateBytes) {
		this.parentCrateFile = parentCrateFile;
		this.parentCrateName = parentCrateName;
		this.crateBytes = crateBytes;
	}

}
