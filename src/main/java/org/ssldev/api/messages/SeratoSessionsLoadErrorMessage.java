package org.ssldev.api.messages;

import org.ssldev.core.messages.Message;

/**
 * describes an error that occured while trying to load sessions
 */
public class SeratoSessionsLoadErrorMessage extends Message {

	public final String error;

	public SeratoSessionsLoadErrorMessage(String error) {
		this.error = error;
	}
}
