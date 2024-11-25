package org.ssldev.api.fields;

import org.ssldev.api.consumption.strategies.StringConsumeStrategy;

public class Adat17CommentField extends Field <Adat17CommentField>{

	public static final String ID = "17";
	
	public Adat17CommentField() {
		super("Comment",17);
		consume = new StringConsumeStrategy();
	}

}
