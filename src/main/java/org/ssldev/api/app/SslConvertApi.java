package org.ssldev.api.app;

import org.ssldev.api.services.BytesToCrateConverterService;
import org.ssldev.api.services.CrateConvertService;
import org.ssldev.api.services.SeratoDbConvertService;
import org.ssldev.api.services.SeratoDbFileUnmarshalService;
import org.ssldev.core.messages.MessageIF;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.utils.Logger;

/**
 * API for converting SSL binary files into POJOs
 */
public class SslConvertApi {

	private EventHub hub;

	public SslConvertApi(EventHub hub) {
		
		this.hub = hub;
		
		Logger.enableDebug(false); 
		Logger.enableFinest(false); 
		Logger.enableTrace(false);
		Logger.setShowTime(true);
		
		registerAllservices(hub);
		hub.init();
		hub.start();
	}

	@SuppressWarnings("unused")
	private void registerAllservices(EventHub hub) {
		CrateConvertService crateConvertService = new CrateConvertService(hub); 
		SeratoDbConvertService dbConvertService = new SeratoDbConvertService(hub);
		SeratoDbFileUnmarshalService dbFileUnmarshallerService = new SeratoDbFileUnmarshalService(hub);
//		SslFileToBytesService sslFileReader = new SslFileToBytesService(hub);
//		SslByteConsumerService byteConsumer = new SslByteConsumerService(hub);
		BytesToCrateConverterService dataConsumer = new BytesToCrateConverterService(hub);
	}

	public void accept(MessageIF msg) {
		hub.add(msg);
	}
}
