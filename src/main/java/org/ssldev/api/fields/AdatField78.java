package org.ssldev.api.fields;

import org.ssldev.api.consumption.strategies.RawStringConsumeStrategy;

/**
 * appeared with serato 2.2.0 update.  not sure what it is
 */
public class AdatField78 extends Field<AdatField78>{

	public AdatField78() {
		super("Field78",78);
		consume = new RawStringConsumeStrategy();
//		consume = new LongOrIntConsumeStrategy();;
	}
}
