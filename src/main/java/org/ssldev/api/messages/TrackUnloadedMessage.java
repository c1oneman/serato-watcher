package org.ssldev.api.messages;

import org.ssldev.api.chunks.Adat;
import org.ssldev.core.messages.Message;

/**
 * The track (in {@link Adat} form) has been unloaded from a deck
 */
public class TrackUnloadedMessage extends Message {
	public final long removeTime;
	public final int deck;
	public final Adat adatRemoved;
	
	public TrackUnloadedMessage(long removeTime, int deck, Adat adatRemoved) {
		this.removeTime = removeTime;
		this.deck = deck;
		this.adatRemoved = adatRemoved;
	}

}
