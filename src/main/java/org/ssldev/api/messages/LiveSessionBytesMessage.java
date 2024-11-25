package org.ssldev.api.messages;

import java.io.File;

import org.ssldev.api.consumption.SslByteConsumer;
import org.ssldev.core.messages.Message;
import org.ssldev.core.utils.Validate;

/**
 * represents the last reported bytes of a live session file (currently playing session)
 *  that is either just started (created) or modified.  
 * <p>
 * If modified, only the modified bytes portion is stored.
 */
public class LiveSessionBytesMessage extends Message {

	public final File sessionFile;
	public final SslByteConsumer byteConsumer;

	public LiveSessionBytesMessage(SslByteConsumer byteConsumer, File sessionFile) {
		Validate.notNull(byteConsumer, "byte consumer cannot be null");
		Validate.notNull(sessionFile, "session file cannot be null");
		
		this.byteConsumer = byteConsumer;
		this.sessionFile = sessionFile;
	}

}
