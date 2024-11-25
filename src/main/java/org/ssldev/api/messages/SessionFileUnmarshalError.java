package org.ssldev.api.messages;

import static java.util.Objects.requireNonNull;

import java.io.File;

import org.ssldev.core.messages.Message;

/**
 * encountered an error while attempting to unmarshal the given file 
 */
public class SessionFileUnmarshalError extends Message {

	public final File sessionFile;
	public final String errorMsg;

	public SessionFileUnmarshalError(File sessionFile, String errorMsg) {
		this.sessionFile = requireNonNull(sessionFile);
		this.errorMsg = errorMsg == null ? "" : errorMsg;
	}

}
