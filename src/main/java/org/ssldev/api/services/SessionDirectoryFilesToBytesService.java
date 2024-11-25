package org.ssldev.api.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.ssldev.api.consumption.SslBuffer;
import org.ssldev.api.consumption.SslByteConsumer;
import org.ssldev.api.messages.LiveSessionBytesMessage;
import org.ssldev.api.messages.SessionFileCreatedMessage;
import org.ssldev.api.messages.SessionFileModifiedMessage;
import org.ssldev.core.messages.MessageIF;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;
import org.ssldev.core.utils.Logger;

/**
 * Listens for Serato session files that are either modified or created (in session directory)
 * and converts them into an SSL bytes buffer.
 * <p>
 * Keeps track of files last-read position so that only the modified portion will get 
 * converted.  The already converted portion can be discarded.
 * <p>
 * Consumes: 
 * <ul>
 * <li>{@link SessionFileCreatedMessage}</li>
 * <li>{@link SessionFileModifiedMessage}</li>
 * </ul>
 * Produces:
 * <ul>
 * <li>{@link LiveSessionBytesMessage}</li>
 * </ul>
 */
public class SessionDirectoryFilesToBytesService extends Service {
	private HashMap<String, Integer> fileToLastByteProcd = new HashMap<>();

	public SessionDirectoryFilesToBytesService(EventHub hub) {
		super(hub);
	}

	@Override
	public void notify(MessageIF msg) {
		if ((msg instanceof SessionFileCreatedMessage)) {
			handleSessionFileCreated(((SessionFileCreatedMessage)msg).sessionFile);
		}
		else if ((msg instanceof SessionFileModifiedMessage)) {
			handleSessionFileModified(((SessionFileModifiedMessage)msg).sessionFile);
		}
	}

	synchronized private void handleSessionFileCreated(File sessionFile) {
		try {
			SslBuffer bytes = new SslBuffer(sessionFile);
			recordSessionLength(bytes.size(), sessionFile.getName());
			publishSessionBytes( bytes, sessionFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException iae) {
			Logger.error(this, "error encountered while unmarshalling ["+sessionFile.getAbsolutePath()+"]",iae);
		}
	}
	
	synchronized private void handleSessionFileModified(File sessionFile) {

		try {
			SslBuffer bytes = new SslBuffer(sessionFile);
			int currSize = bytes.size();
			fastForwardBufferOnUpdate(bytes, sessionFile);
			
			if(!bytes.isEmpty()) {
				recordSessionLength(currSize, sessionFile.getName());
				publishSessionBytes(bytes, sessionFile);
			}

		} catch (IOException e) {
			Logger.error(this, e.toString());
			e.printStackTrace();
		} catch (IllegalArgumentException iae) {
			Logger.warn(this,
					"Illegal argument exception while processing modification of ["+sessionFile.getAbsolutePath()+"]. "
					+ "(expected if happened at SSL shutdown due to file compacting: "
							+ iae.getMessage());
		}
	}
	
	
	private void recordSessionLength(int size, String sessionFileName) {
		Logger.debug(this, sessionFileName + " contains " + size + "/" + (size - getStartPosition(sessionFileName)) + " (total/modified) bytes.");

		fileToLastByteProcd.put(sessionFileName, size);
	}

	// notify that session data was consumed and is ready to be processed
	private void publishSessionBytes(SslBuffer bytes, File sessionFile) {
		hub.add(new LiveSessionBytesMessage(new SslByteConsumer(bytes), sessionFile));
	}
	
	private void fastForwardBufferOnUpdate(SslBuffer bytes, File sslFile) {
		// compute start position if this file was already processed
		int startAt = getStartPosition(sslFile.getName());
		
		Logger.debug(this, "fast forwarding ["+sslFile.getName()+"] to: " + startAt +"(bytes size= "+
							bytes.size()+")");
		bytes.fastforward(startAt);
		Logger.debug(this, "after fast forwarding ["+sslFile.getName()+"] size is: " +bytes.size());
	}

	private int getStartPosition(String name) {
		return fileToLastByteProcd.containsKey(name) ? fileToLastByteProcd.get(name) : 0;
	}
}
