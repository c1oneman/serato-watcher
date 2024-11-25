package org.ssldev.api.services;

import java.io.File;
import java.util.List;

import org.ssldev.api.chunks.Otrk;
import org.ssldev.api.consumption.SslBuffer;
import org.ssldev.api.consumption.SslByteConsumer;
import org.ssldev.api.messages.SeratoDbFileUnmarshalled;
import org.ssldev.api.messages.SessionFileUnmarshalError;
import org.ssldev.api.messages.UnmarshalSeratoDbFileRequest;
import org.ssldev.api.utils.DataExtractUtil;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;
import org.ssldev.core.utils.Logger;
import org.ssldev.core.utils.StopWatch;
/**
 * handles a request to convert a db file into bytes.
 * <p> 
 * Consumes: 
 * <ul>
 * <li>{@link UnmarshalSeratoDbFileRequest}</li>
 * </ul>
 * Produces:
 * <ul>
 * <li>{@link SeratoDbFileUnmarshalled}</li>
 * <li>{@link SessionFileUnmarshalError}</li>
 * </ul>
 */
public class SeratoDbFileUnmarshalService extends Service {

	public SeratoDbFileUnmarshalService(EventHub hub) {
		super(hub);
	}

	@Override
	public void init() {
		hub.register(UnmarshalSeratoDbFileRequest.class, msg -> handleUnmarshalDbFileRequest(msg.dbFile));
	}
	
	
	private void handleUnmarshalDbFileRequest(File dbFile) {
		
		try {
			
			StopWatch unmarshalTimer = new StopWatch().start();
			SslBuffer buf = new SslBuffer(dbFile);
			SslBuffer bufCopy = new SslBuffer(buf);
			
			SslByteConsumer dbBytes = new SslByteConsumer(buf);
			
			List<Otrk> otrks = DataExtractUtil.extractOtrk(dbBytes);
			
			if(Logger.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder("DB file contents unmarshalled ["+otrks.size()+" Otrks]:\n");
				otrks.forEach(o -> sb.append(o).append("\n"));
				Logger.debug(this, sb.toString());
			}

			Logger.debug(this, "finished unmarshalling " + dbFile.getAbsolutePath() 
			+ " ["+otrks.size()+" entries]["+dbFile.length() / 1000+" kb] ("+unmarshalTimer.stop()+")");
			
			hub.add(new SeratoDbFileUnmarshalled(dbFile, dbBytes, bufCopy));
		} catch (Throwable ioe) {
			Logger.error(this, "error encountered while unmarshalling ["+dbFile.getAbsolutePath()+"]",ioe);
			hub.add(new SessionFileUnmarshalError(dbFile, ioe.getMessage()));
		}
	}

}
