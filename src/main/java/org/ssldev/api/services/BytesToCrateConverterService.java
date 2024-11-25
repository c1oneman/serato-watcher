package org.ssldev.api.services;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.List;

import org.ssldev.api.chunks.Otrk;
import org.ssldev.api.chunks.Ovct;
import org.ssldev.api.chunks.Ptrk;
import org.ssldev.api.chunks.Tvcn;
import org.ssldev.api.consumption.ByteConsumerIF;
import org.ssldev.api.consumption.SslByteConsumer;
import org.ssldev.api.messages.CrateBytesConsumedMessage;
import org.ssldev.api.messages.CrateConvertedMessage;
import org.ssldev.api.messages.CratesBytesConsumedMessage;
import org.ssldev.api.messages.CratesConvertedMessage;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;
import org.ssldev.core.utils.Logger;

/**
 * publishes all {@link Ptrk} consumed as part of a crate, in a {@link CrateConvertedMessage}. <p>  
 * Each {@link Ptrk} represents a track in a crate. <p>
 * 
 * Consumes: 
 * <ul>
 * <li>{@link CrateBytesConsumedMessage}</li>
 * <li>{@link CratesBytesConsumedMessage}</li>
 * </ul>
 * Produces:
 * <ul>
 * <li>{@link CrateConvertedMessage}</li>
 * <li>{@link CratesConvertedMessage}</li>
 * </ul>
 */
public class BytesToCrateConverterService extends Service{

	public BytesToCrateConverterService(EventHub hub) {
		super(hub);
	}

	@Override
	public void init() {
		hub.register(CrateBytesConsumedMessage.class, m -> process(m));
		hub.register(CratesBytesConsumedMessage.class, m -> process(m));
	}
	
//	@Override
//	public void notify(MessageIF msg) {
//		if(!(msg instanceof CrateBytesConsumedMessage)) return;
//		
//		CrateBytesConsumedMessage cMsg = (CrateBytesConsumedMessage) msg;
//		
//		process(cMsg.consumer, cMsg.fileName);
//	}
	
	private void process(CrateBytesConsumedMessage msg) {
		hub.add( process(msg.consumer, msg.crateFile, msg.crateName));
	}
	
	private void process(CratesBytesConsumedMessage msg) {
		
			List<CrateConvertedMessage> all = msg.crateBytes.stream()
					.map(c -> process(c, msg.parentCrateFile, msg.parentCrateName))
					.collect(toList());
			
			List<Ptrk> allTracks = all.stream()
				.map(m -> m.ptrks)
				.flatMap(List::stream)
				.collect(toList());
			
			hub.add(new CratesConvertedMessage(msg.parentCrateFile, msg.parentCrateName, all.get(0).displayedColumns, allTracks));
	}

	private CrateConvertedMessage process(ByteConsumerIF sslData, File crateFile, String crateName) {
		/*
		 * currently structure is as follows:
		 * [1] crate consumer
		 * -->[1] vrsn
		 * -->[1] osrt
		 * 		-->[1] tvcn   // sort-by column name
		 * 		-->[1] brev	  // is descending
		 * -->[*] ovct
		 * 		-->[1] tvcn   // column name
		 * 		-->[1] tvcw   // column width?
		 */
		if(!(sslData instanceof SslByteConsumer)) {
			throw new IllegalStateException("structure may have changed. assumed top level consumer is SslByteConsumer. instead got "+sslData.getClass().getSimpleName());
		}
		SslByteConsumer ssl = (SslByteConsumer)sslData;
		List<Tvcn> displayedColumns = ssl.getData().stream()
				.filter(Ovct.class::isInstance).map(Ovct.class::cast)
				.map(Ovct::getData)
				.flatMap(ovct -> ovct.stream())
				.filter(Tvcn.class::isInstance).map(Tvcn.class::cast)
				.collect(toList());
		
		List<Ptrk> ptrks = ssl.getData().stream()
				.filter(Otrk.class::isInstance).map(Otrk.class::cast)
				.map(Otrk::getData)
				.flatMap(ptrk -> ptrk.stream())
				.filter(Ptrk.class::isInstance).map(Ptrk.class::cast)
				.collect(toList());
		
		Logger.debug(this, 
				crateName + System.lineSeparator() 
				     + " contained display columns:"+System.lineSeparator()
				     + "  " + displayedColumns+System.lineSeparator()
				     +" contained "+ptrks.size()+" tracks:"+System.lineSeparator()
				     + "  " + ptrks);
		
		return new CrateConvertedMessage(crateFile, crateName, displayedColumns, ptrks);
	}

}
