package org.ssldev.api.messages;

import org.ssldev.api.chunks.Adat;
import org.ssldev.core.messages.Message;

/**
 * the track (in {@link Adat} form) got updated
 */
public class TrackUpdatedMessage extends Message {
	
	public final int deck;
	public final Adat adat;
	public final long lastUpdateTime;

	/**
	 * @param lastUpdateTime  reported for this track
	 * @param adat track updated
	 * @param deck deck track is loaded on
	 */
	public TrackUpdatedMessage(long lastUpdateTime, Adat adat, int deck) {
		this.lastUpdateTime = lastUpdateTime;
		this.adat = adat;
		this.deck = deck;
	}

}
