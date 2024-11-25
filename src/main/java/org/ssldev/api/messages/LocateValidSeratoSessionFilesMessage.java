package org.ssldev.api.messages;

import java.io.File;

import org.ssldev.core.messages.Message;
import org.ssldev.core.utils.Validate;

/**
 * request to locate all valid session files per the given
 * criteria
 */
public class LocateValidSeratoSessionFilesMessage extends Message {
	public int maxAgeInMonths;
	public int maxFileSizeInBytes;
	public int minFileSizeInBytes;
	public File sessionsFileDirLoc;
	
	public LocateValidSeratoSessionFilesMessage(File sessionsDir) {
		Validate.exists(sessionsDir);
		sessionsFileDirLoc = sessionsDir;
	}
}
