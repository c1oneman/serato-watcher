package org.ssldev.api.messages;

import java.io.File;

import org.ssldev.core.messages.Message;
import org.ssldev.core.utils.Validate;

/**
 * signals that a binary Serato session file got added to the watched directory.
 */
public class SessionFileCreatedMessage extends Message {

	public File sessionFile;
	
	public SessionFileCreatedMessage(File sessionFile) {
		Validate.exists(sessionFile);
		this.sessionFile = sessionFile;
	}
	
	@Override
	public String toString() {
		return super.toString() + ": "+sessionFile.getName();
	}
}
