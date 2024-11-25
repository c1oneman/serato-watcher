package org.ssldev.api.messages;

import java.io.File;

import org.ssldev.core.messages.Message;
import org.ssldev.core.utils.Validate;
/**
 * represents request to unmarshal an SSL history session file
 * into collection of tracks 
 */
public class UnmarshalSessionRequestMessage extends Message {
	public final File sessionFile;

	public UnmarshalSessionRequestMessage(File sessionFile) {
		Validate.notNull(sessionFile, "session file cannot be null");
		Validate.isTrue(sessionFile.exists(), sessionFile.getAbsolutePath() + " must exist");
		
		this.sessionFile = sessionFile;
	}
	
	@Override
	public String getName() {
		return super.getName() + " [" + sessionFile.getName() + "][" + sessionFile.length() / 1000 + " kb]";
	}
}
