package org.ssldev.api.services;

import java.io.File;
import java.util.List;

import org.ssldev.api.chunks.Adat;
import org.ssldev.api.consumption.SslBuffer;
import org.ssldev.api.consumption.SslByteConsumer;
import org.ssldev.api.messages.SessionFileUnmarshalError;
import org.ssldev.api.messages.SessionFileUnmarshalledMessage;
import org.ssldev.api.messages.UnmarshalMultipleSessionsRequest;
import org.ssldev.api.messages.UnmarshalSessionRequestMessage;
import org.ssldev.api.utils.DataExtractUtil;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;
import org.ssldev.core.utils.Logger;
import org.ssldev.core.utils.StopWatch;
/**
 * handles a request to convert a session file into bytes.
 * <p> 
 * Consumes: 
 * <ul>
 * <li>{@link UnmarshalSessionRequestMessage}</li>
 * <li>{@link UnmarshalMultipleSessionsRequest}</li>
 * </ul>
 * Produces:
 * <ul>
 * <li>{@link SessionFileUnmarshalledMessage}</li>
 * </ul>
 */
public class SslSessionFileUnmarshalService extends Service {

	public SslSessionFileUnmarshalService(EventHub hub) {
		super(hub);
	}

	@Override
	public void init() {
		hub.register(UnmarshalMultipleSessionsRequest.class, this::handleUnmarshalSessionsRequest);
		hub.register(UnmarshalSessionRequestMessage.class, msg -> handleUnmarshalSessionRequest(msg.sessionFile));
	}
	

	private void handleUnmarshalSessionsRequest(UnmarshalMultipleSessionsRequest req) {
		
		int size = req.sessionsToUnmarshal.size();
		
		Logger.info(this, "about to unmarshal ["+size+"] session files...");
		
		if(size < 20) {
			Logger.debug(this, "executing unmarshalling sequentially...");
			req.sessionsToUnmarshal.forEach(this::handleUnmarshalSessionRequest);
		}
		else {
			StopWatch parallelExecTimer = new StopWatch().start();
			Logger.debug(this, "executing unmarshalling in parallel...");
			
			req.sessionsToUnmarshal.parallelStream().forEach(this::handleUnmarshalSessionRequest);
			
			Logger.debug(this, "finished unmarshalling all session files in ["+parallelExecTimer.stop()+"]");
		}
		
	}
	
	private void handleUnmarshalSessionRequest(File sessionFile) {
		
		try {
			SslByteConsumer sessionBytes = new SslByteConsumer(new SslBuffer(sessionFile));
			List<Adat> adats = DataExtractUtil.extractAdat(sessionBytes);
			
			Logger.debug(this, "finished unmarshalling " + sessionFile.getAbsolutePath() + " ["+sessionFile.length() / 1000+" kb]");
			
			hub.add(new SessionFileUnmarshalledMessage(sessionFile, adats));
		} catch (Throwable ioe) {
			Logger.error(this, "error encountered while unmarshalling ["+sessionFile.getAbsolutePath()+"]",ioe);
			hub.add(new SessionFileUnmarshalError(sessionFile, ioe.getMessage()));
		}
	}

}
