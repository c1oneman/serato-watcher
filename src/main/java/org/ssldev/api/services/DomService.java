package org.ssldev.api.services;

import java.io.File;
import java.io.IOException;

import org.ssldev.api.consumption.ByteConsumerIF;
import org.ssldev.api.consumption.SslByteConsumer;
import org.ssldev.api.messages.LiveSessionBytesMessage;
import org.ssldev.core.messages.MessageIF;
import org.ssldev.core.messages.WriteToFileMessage;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;
import org.ssldev.core.utils.Logger;
/**
 * maintains DOM representation of all OENTs procd per current session.
 * Currently the DOM only dumps all procd OENTs to a file and does not maintain model state.
 * Dump includes all tracks in the entire active session, not just ones procd after starting this app.
 * 
 * Consumes: 
 * <ul>
 * <li>{@link LiveSessionBytesMessage}</li>
 * </ul>
 * Produces: N/A
 */
public class DomService extends Service {
	private final File theDom;

	public DomService(EventHub hub, String domFilePath) {
		super(hub);
		
		theDom = new File(domFilePath);
	}
	
	@Override
	public void init() {
		// create the DOM
		if(!theDom.exists()) {
			File parentFile = theDom.getParentFile();
			if(null == parentFile) {
				Logger.error(this, "cannot create DOM parent with path given: "+theDom.getAbsolutePath());
				return;
			}
			if(!parentFile.exists() && !parentFile.mkdirs()) {
				Logger.error(this, "cannot create DOM parent dir structure with path given: "+theDom.getAbsolutePath());
				return;
			}

			try {
				// delete old DOM
				if(!theDom.delete()) {
					Logger.warn(this, "was not able to delete the DOM.  Current session data will be appended to previous!");
				}

				theDom.createNewFile();
			} catch (IOException e) {
				Logger.error(this, "cannot create DOM with path given: "+theDom.getAbsolutePath() + " due to "+e);
				return;
			}
		}
	}

	@Override
	public void notify(MessageIF msg) {
		if(null == theDom || !(msg instanceof LiveSessionBytesMessage)) return;
		
		
		process(((LiveSessionBytesMessage)msg).byteConsumer);
	}

	private void process(SslByteConsumer consumer) {
		
		Logger.debug(this, "writing " + consumer.getData().size() + " consumed objects to the DOM file");
		for(ByteConsumerIF c : consumer.getData()) {
			hub.add(new WriteToFileMessage(theDom.getAbsolutePath(),c.toString()+"\n"));
		}
	}

}
