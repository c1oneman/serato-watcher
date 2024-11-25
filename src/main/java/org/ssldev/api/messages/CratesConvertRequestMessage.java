package org.ssldev.api.messages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ssldev.core.messages.Message;

/**
 * request to convert the given crate files to text
 */
public class CratesConvertRequestMessage extends Message{
	
	public final File parentCrateFile;
	public final String parentCrateName;
	public final List<File> crates;
	
	public CratesConvertRequestMessage(File parentCrateFile, String parentCrateName, List<File> crates) {
		this.parentCrateFile = parentCrateFile;
		this.crates = new ArrayList<>(crates);
		this.parentCrateName = parentCrateName;
	}
}
