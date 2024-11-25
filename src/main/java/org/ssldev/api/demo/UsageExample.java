package org.ssldev.api.demo;

import org.ssldev.api.app.SslApi;
import org.ssldev.api.messages.TrackLoadedMessage;
import org.ssldev.api.messages.TrackUnloadedMessage;

/**
 * demonstrates usage of the ssl-api.
 */
public class UsageExample {

	public static void main(String[] args) {
		/*
		 * 1. instantiate the ssl-api 
		 */
		SslApi api = new SslApi();
		
		
		/*
		 * 2. register a service that will simply print out incoming tracks loaded/ejected.
		 */
		api.register( msg -> System.out.println("DEMO APP got notified of: " + msg), 
					  // subscribe for track loading and ejecting notifications 
					  TrackLoadedMessage.class, TrackUnloadedMessage.class );
		
		/*
		 * 3. start the API.  The API will start listening for Serato session 
		 * file changes, and publish out play data as events (e.g. TrackLoadedMessage)
		 */
		api.start();
	}

}
