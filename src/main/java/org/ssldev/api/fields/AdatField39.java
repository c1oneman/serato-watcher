package org.ssldev.api.fields;

import org.ssldev.api.consumption.strategies.BooleanConsumeStrategy;

public class AdatField39 extends Field <AdatField39 >{

	public AdatField39() {
		super("Field39",39);
		consume = new BooleanConsumeStrategy();
	}
	
}
