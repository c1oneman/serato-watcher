package org.ssldev.api.messages;

import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.ssldev.api.SeratoSessionFile;
import org.ssldev.core.messages.Message;

/**
 * signifies that the given class has finished loading all applicable
 * Serato session files.
 */
public class SeratoSessionsLoadCompleteMessge extends Message {

	public final Map<String, SeratoSessionFile> loadedSessionFiles;
	public final Class<?> clazz;

	/**
	 * {@link SeratoSessionsLoadCompleteMessge} constructor
	 * 
	 * @param loadedSessionFiles that were loaded by the given class
	 * @param clazz that loaded the session files
	 */
	public SeratoSessionsLoadCompleteMessge(Map<String, SeratoSessionFile> loadedSessionFiles,
			Class<?> clazz) {
		
		this.loadedSessionFiles = requireNonNull(loadedSessionFiles);;
		this.clazz = requireNonNull(clazz);
	}

}
