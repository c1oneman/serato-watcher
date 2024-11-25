package org.ssldev.api.services;

import java.util.concurrent.atomic.AtomicInteger;

import org.ssldev.api.gui.NowPlayingAppGUI;
import org.ssldev.api.messages.TrackLoadedMessage;
import org.ssldev.api.messages.TrackUnloadedMessage;
import org.ssldev.core.messages.MessageIF;
import org.ssldev.core.mgmt.AsyncEventHub;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;
import org.ssldev.core.utils.Logger;

import javafx.application.Application;

/**
 * Launches the GUI service. Also acts as a controller of sorts by updating the 
 * GUI with incoming model messages.
 * <p>
 * NOTE: utilizes restricted JavaFX API calls.  may break with future updates!
 * <p>
 * Consumes: 
 * <ul>
 * <li>{@link TrackLoadedMessage}</li>
 * </ul>
 * Produces: N/A
 */
public class NowPlayingGuiService extends Service {
	
	private AtomicInteger trackMsgCounter = new AtomicInteger(1);
	
	private static EventHub HUB_INSTANCE;

	public NowPlayingGuiService(EventHub hub) {
		super(hub);
		NowPlayingGuiService.HUB_INSTANCE = hub;
	}
	
	@Override
	public void init() {
		if(!(hub instanceof AsyncEventHub)) {
			Logger.error(this, "GUI service requires an async event hub. Service will not start.");
			return;
		}
		
		// only happens once...
		hub.invokeLater(NowPlayingGuiService::startGui);
	}
	
	private static void startGui() {
		Thread.currentThread().setName("THREAD - GUI launch service");
		// start the GUI
		try {
			// note: this is a restricted call. may break with future updates!
			Application.launch(NowPlayingAppGUI.class, new String[0]);
			// GUI has exited.. tell model to shutdown
			HUB_INSTANCE.shutdown();
			Logger.info(NowPlayingGuiService.class, "GUI exit...");
		} catch(IllegalStateException e) {
			Logger.warn(NowPlayingGuiService.class, "cannot start SSL API GUI more than once.");
		}
	}

	@Override
	public void notify(MessageIF msg) {
		if(msg instanceof TrackLoadedMessage) {
			TrackLoadedMessage m = (TrackLoadedMessage) msg;
			NowPlayingAppGUI.appendToNowPlaying(trackMsgCounter.getAndIncrement() + ": [LOADED] "+m.adat);
			NowPlayingAppGUI.setCueLabelText(m.adat.getArtist() + " - "+ m.adat.getTitle());
		}
		else if(msg instanceof TrackUnloadedMessage) {
			TrackUnloadedMessage m = (TrackUnloadedMessage) msg;
			NowPlayingAppGUI.appendToNowPlaying(trackMsgCounter.getAndIncrement() + ": [EJECTED] "+m.adatRemoved);
		}
	}

}
