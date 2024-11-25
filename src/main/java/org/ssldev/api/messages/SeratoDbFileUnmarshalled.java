package org.ssldev.api.messages;

import java.io.File;

import org.ssldev.api.consumption.SslBuffer;
import org.ssldev.api.consumption.SslByteConsumer;
import org.ssldev.core.messages.Message;
import org.ssldev.core.utils.Validate;

/**
 * the DB file was unmarshalled into the given list of entries
 */
public class SeratoDbFileUnmarshalled extends Message {
	/** session file unmarshalled */
	public final File dbFile;
	public final SslByteConsumer dbBytes;
	public final SslBuffer origSslBuffer;

	public SeratoDbFileUnmarshalled(File sessionFile, SslByteConsumer dbBytes, SslBuffer origSslBuffer) {
		Validate.notNull(sessionFile, "session file cannot be null");
		Validate.notNull(dbBytes, "db bytes unmarshalled cannot be null");
		Validate.notNull(origSslBuffer, "SSL buffer cannot be null");
		
		this.dbFile = sessionFile;
		this.dbBytes = dbBytes;
		this.origSslBuffer = origSslBuffer;
//		this.otrks = otrks;
//		this.seratoSession = new SeratoSessionFile(sessionFile, adats);
	}
}
