package org.ssldev.api.messages;

import java.io.File;

import org.ssldev.core.messages.Message;
import org.ssldev.core.utils.Validate;

/**
 * signals that a binary SSL session file got modified
 */
public class SessionFileModifiedMessage extends Message {

	public File sessionFile;
	
	public SessionFileModifiedMessage(File sessionFile) {
		Validate.exists(sessionFile);
		this.sessionFile = sessionFile;
	}
	
	@Override
	public String toString() {
		return super.toString() + ": "+sessionFile.getName();
	
	}
}
