package org.ssldev.api.fields;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ssldev.api.consumption.strategies.LongOrIntConsumeStrategy;

public class Adat53UpdatedAtField extends Field <Adat53UpdatedAtField>{

	public static final String ID = "53";

	public Adat53UpdatedAtField() {
		super("Updated At",53);
		consume = new LongOrIntConsumeStrategy();
	}
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name); 
		sb.append(": ").append(sdf.format(new Date(((Number)data).longValue()*1000)));
		return sb.toString();
	}

}
