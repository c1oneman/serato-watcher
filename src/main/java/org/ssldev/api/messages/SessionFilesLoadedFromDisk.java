package org.ssldev.api.messages;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.ssldev.api.SeratoSessionFile;
import org.ssldev.core.messages.Message;

/**
 * a service has loaded these {@link SeratoSessionFile} files from disk. usually happens on startup
 */
public class SessionFilesLoadedFromDisk extends Message {

	public final Map<String, SeratoSessionFile> loadedSessionFiles;

	public SessionFilesLoadedFromDisk(HashMap<String, SeratoSessionFile> loadedSessionFiles) {
		this.loadedSessionFiles = Collections.unmodifiableMap(loadedSessionFiles);
	}

}
