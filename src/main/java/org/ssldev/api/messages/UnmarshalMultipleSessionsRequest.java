package org.ssldev.api.messages;

import static java.util.Collections.unmodifiableList;

import java.io.File;
import java.util.List;

import org.ssldev.core.messages.Message;
import org.ssldev.core.utils.Validate;

/**
 * request to unmarshal multiple session files
 */
public class UnmarshalMultipleSessionsRequest extends Message {

	public final List<File> sessionsToUnmarshal;

	public UnmarshalMultipleSessionsRequest(List<File> sessionsToUnmarshal) {
		Validate.notNull(sessionsToUnmarshal, "sessions to unmarshal cannot be null");
		
		this.sessionsToUnmarshal = unmodifiableList(sessionsToUnmarshal);
	}

}
