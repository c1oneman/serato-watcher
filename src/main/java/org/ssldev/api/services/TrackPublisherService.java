package org.ssldev.api.services;

import java.util.HashMap;
import java.util.Map;

import org.ssldev.api.chunks.Adat;
import org.ssldev.api.messages.AdatConsumedMessage;
import org.ssldev.api.messages.TrackLoadedMessage;
import org.ssldev.api.messages.TrackUnloadedMessage;
import org.ssldev.api.messages.TrackUpdatedMessage;
import org.ssldev.api.messages.TrackStatusMessage;
import org.ssldev.api.messages.TrackStatusMessage.Status;
import org.ssldev.api.chunks.AdatC;
import org.ssldev.core.messages.MessageIF;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;
import org.ssldev.core.utils.Logger;
import org.ssldev.core.utils.SysInfo;
/**
 * models a set of decks and publishes out whenever Serato reports a track was
 * loaded/unloaded from a deck, or track update was received. 
 * <p>
 * In practice, Serato (2.1.0) publishes a track 'add' when a track first gets
 * loaded into an empty deck.  And a track 'remove' followed by a track 'add'
 * when a loaded track is replaced with another.  Track updates seem to 
 * happen at infrequent intervals.
 * <p>
 * It should be noted that the data that Serato reports (and the publishing)
 * may be different in different play modes (e.g. offline, connected)
 * 
 * Consumes: 
 * <ul>
 * <li>{@link AdatConsumedMessage}</li>
 * </ul>
 * Produces:
 * <ul>
 * <li>{@link TrackLoadedMessage}</li>
 * <li>{@link TrackUnloadedMessage}</li>
 * <li>{@link TrackUpdatedMessage}</li>
 * <li>{@link TrackStatusMessage}</li>
 * </ul>
 */
public class TrackPublisherService extends Service {
	private Map<Integer,Deck> decks = new HashMap<>();
	
	private static final Adat EMPTY = new Adat();
	
	/** ensure serial track processing */
	private final Object lock = new Object();
	private boolean isInitialLoad = true;

	public TrackPublisherService(EventHub hub) {
		super(hub);
	}
	
	public void notify(MessageIF msg) {
		if(!(msg instanceof AdatConsumedMessage)) return;
		
		synchronized (lock) {
			if (isInitialLoad) {
				process(((AdatConsumedMessage)msg).adat, true);
				isInitialLoad = false;
			} else {
				process(((AdatConsumedMessage)msg).adat, false);
			}
		}
	}

	private void process(Adat adat, boolean skipPublish) {
		decks.computeIfAbsent(adat.getDeck(), Deck::new)
			 .onChange(adat, skipPublish);
	}

	private class Deck{
		private final int deck;
		private Adat loaded = EMPTY;
		private double volume = 1.0; // Default volume
		
		public Deck(int num) {
			deck = num;
		}

		/**
		 * notification that an adat update was received for 
		 * this deck
		 * @param adat
		 */
		public void onChange(Adat adat, boolean skipPublish) {
			/*
			 * 1. cue tack removed
			 * 2. cue track added
			 * 3. track playing add / playing update
			 */
			if(isRemoved(adat)) {
				// track has been removed
				onRemove(adat, skipPublish);
			}
			else if(isAdded(adat)) {
				// track has been added
				onAdd(adat, skipPublish);
			}
			else {
				// track was updated
				onUpdate(adat, skipPublish);
			}
		}

		private void onUpdate(Adat adat, boolean skipPublish) {
			if (!skipPublish) {
				publishTrackUpdated(adat);
				publishTrackStatus(adat, Status.STARTED);
			}
		}

		private void onAdd(Adat adat, boolean skipPublish) {
			if(!loaded.getTitle().equals(adat.getTitle())) {
				loaded = adat;
				if (!skipPublish) {
					publishTrackLoaded(adat);
					publishTrackStatus(adat, Status.STARTED);
				}
			}
			else {
				onUpdate(adat, skipPublish);
			}
		}

		private void onRemove(Adat adat, boolean skipPublish) {
			loaded = EMPTY;
			if (!skipPublish) {
				publishTrackUnloaded(adat);
			}
		}

		
		// currently serato sets the update time equal to the 'startTime', when a 
		// track is first added to a deck
		private boolean isAdded(Adat adat) {
			return adat.getStartTime() == adat.getUpdateTime();
		}

		// currently serato updates an Adat's 'totalPlayTime' field when it is removed
		// from a deck
		private boolean isRemoved(Adat adat) {
			return adat.getTotalPlayTime() > 0;
		}
		

		private void publishTrackLoaded(Adat adat) {
			Logger.info(this, toString() + "[Added] "+adat.getArtist() +" - " +adat.getTitle() + " ["+adat.getBpm()+"]");
			Logger.debug(this, adat.toString());
			hub.add(new TrackLoadedMessage(adat.getStartTime(), deck, adat));
		}

		private void publishTrackUpdated(Adat adat) {
			Logger.info(this, toString() + "[Update] "+adat.getArtist() +" - " +adat.getTitle() + " ["+adat.getBpm()+"]");
			Logger.debug(this, adat.toString());
			hub.add(new TrackUpdatedMessage(adat.getUpdateTime(), adat, deck));
		}
		
		private void publishTrackUnloaded(Adat adat) {
			Logger.info(this, toString() + "[Remove] "+adat.getArtist() +" - " +adat.getTitle() + " ["+adat.getBpm()+"]");
			Logger.debug(this, adat.toString());
			hub.add(new TrackUnloadedMessage(adat.getEndTime(), deck, adat));
		}

		private void publishTrackStatus(Adat adat, Status status) {
			AdatC adatC = new AdatC(adat);
			Logger.info(this, "Publishing TrackStatusMessage: " + status + " for " + adat.getTitle() + " on deck " + deck);
			TrackStatusMessage msg = new TrackStatusMessage(adatC, status, deck, volume);
			hub.add(msg);
			Logger.debug(this, "TrackStatusMessage published: " + convertToJson(msg));
		}

		private String convertToJson(TrackStatusMessage msg) {
			return String.format(
				"{\"status\":\"%s\",\"deck\":%d,\"volume\":%.2f,\"track\":{\"title\":\"%s\",\"artist\":\"%s\"}}",
				msg.getStatus(),
				msg.getDeck(),
				msg.getTrack().getTitle(),
				msg.getTrack().getArtist()
			);
		}
		
		@Override
		public String toString() {
			return "[DECK "+deck+"]";
		}
	}

	@Override
	public void shutdown() {
		super.shutdown();
		decks.clear();
	}
}
