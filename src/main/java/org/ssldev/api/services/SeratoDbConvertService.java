package org.ssldev.api.services;

import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;

/**
 * converts serato DB file into POJOS. <p>
 * 
 */
public class SeratoDbConvertService extends Service 
{

	public SeratoDbConvertService(EventHub hub) {
		super(hub);
	}
	
	
	// * Consumes: 
	// * <ul>
	// * <li>{@link DbConvertRequestMessage}</li>
	// * <li>{@link DbConvertedMessage}</li>
	// * </ul>
	// * Produces:
	// * <ul>
	// * <li>{@link SslFileToBytesMessage}</li>
	// * <li>{@link DbConvertResponseMessage}</li>
	// * </ul>

}
