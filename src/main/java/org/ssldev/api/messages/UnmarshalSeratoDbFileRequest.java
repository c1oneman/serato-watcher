package org.ssldev.api.messages;

import java.io.File;

import org.ssldev.core.messages.Message;
import org.ssldev.core.utils.Validate;
/**
 * represents request to unmarshal a serato DB file
 * into collection of entries 
 */
public class UnmarshalSeratoDbFileRequest extends Message {
	public final File dbFile;

	public UnmarshalSeratoDbFileRequest(File dbFile) {
		Validate.notNull(dbFile, "db file cannot be null");
		Validate.isTrue(dbFile.exists(), dbFile.getAbsolutePath() + " must exist");
		
		this.dbFile = dbFile;
	}
	
	@Override
	public String getName() {
		return super.getName() + " [" + dbFile.getName() + "][" + dbFile.length() / 1000 + " kb]";
	}
}
