package org.ssldev.api.messages;

import org.ssldev.api.chunks.Adat;
import org.ssldev.core.messages.Message;

/**
 * The track (in {@link Adat} form) has been loaded to a deck
 */
public class TrackLoadedMessage extends Message {
	public final long starttime;
	public final int deck;
	public final Adat adat;
	
	public TrackLoadedMessage(long startTime, int deck, Adat adat) {
		this.starttime = startTime;
		this.deck = deck;
		this.adat = adat;
	}
	
}
