package org.ssldev.api.fields;

import org.ssldev.api.consumption.strategies.RawStringConsumeStrategy;

public class AdatField37 extends Field <AdatField33>{

	public AdatField37() {
		super("Field37",37);
		consume = new RawStringConsumeStrategy();
	}

}
