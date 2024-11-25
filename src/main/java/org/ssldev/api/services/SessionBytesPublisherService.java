package org.ssldev.api.services;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;

import org.ssldev.api.chunks.Adat;
import org.ssldev.api.chunks.Oent;
import org.ssldev.api.consumption.ByteConsumerIF;
import org.ssldev.api.consumption.SslByteConsumer;
import org.ssldev.api.messages.AdatConsumedMessage;
import org.ssldev.api.messages.MultipleAdatsConsumedMessage;
import org.ssldev.api.messages.LiveSessionBytesMessage;
import org.ssldev.core.messages.MessageIF;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;
import org.ssldev.core.utils.Logger;
/**
 * publishes ADAT chunks that were consumed as part of a session.  Each Adat 
 * represents a track that was played or cued.
 * <p>
 * {@link MultipleAdatsConsumedMessage}} gets published if multiple ADATs 
 * were consumed (generally happens when the App is started after SSL, 
 * and reads in an already populated history session file)
 * 
 * <p>
 * All chunks consumed are also logged out to given file path.
 * <p>
 * Consumes: 
 * <ul>
 * <li>{@link LiveSessionBytesMessage}</li>
 * </ul>
 * Produces:
 * <ul>
 * <li>{@link AdatConsumedMessage}</li>
 * <li>{@link MultipleAdatsConsumedMessage}</li>
 * </ul>
 */
public class SessionBytesPublisherService extends Service {
	
	public SessionBytesPublisherService(EventHub hub) {
		super(hub);
	}

	@Override
	public void notify(MessageIF msg) {
		if(!(msg instanceof LiveSessionBytesMessage)) return;
		
		LiveSessionBytesMessage cMsg = (LiveSessionBytesMessage) msg;
		
		process(cMsg.byteConsumer);
	}

	private void process(ByteConsumerIF sslData) {
		/*
		 * currently session structure is as follows:
		 * [1]ssl
		 * -->[1] vrsn
		 * -->[*] oent
		 * 			-->[*] adat
		 * 						--> [*] field 
		 */
		if(!(sslData instanceof SslByteConsumer)) {
			throw new IllegalStateException("structure may have changed. assumed top level consumer is SslByteConsumer. instead got "+sslData.getClass().getSimpleName());
		}
		SslByteConsumer ssl = (SslByteConsumer)sslData;
		
		List<Oent> oents = ssl.getData().stream()
							  .filter(Oent.class::isInstance).map(Oent.class::cast)
							  .collect(toList());
		
		// determine if this is a one track or multi-track update
		if(oents.size() == 1) {
			Logger.debug(this,"one oent updated");
			publishAdats(oents.remove(0));
		}
		else if(oents.size() > 1){
			// multi-track happens when reading in a pre-existing history file
			Logger.debug(this,oents.size() +" oents got updated");
			List<Adat> adats = oents.stream()
									.map(Oent::getData).flatMap(Collection::stream)
									.filter(Adat.class::isInstance).map(Adat.class::cast)
									.collect(toList());
			
			Logger.debug(this,"collected "+adats.size() + " adats");
//			hub.add(new MultipleAdatsConsumedMessage(adats));
			oents.forEach(this::publishAdats);
		}
		else {
			Logger.error(this, "no oents were consumed. structure must be bad");
		}
	}
	
	private void publishAdats(Oent c) {
		c.getData().stream()
				   .filter(Adat.class::isInstance)
				   .forEach(adat -> hub.add(new AdatConsumedMessage((Adat) adat)));
	}

}
