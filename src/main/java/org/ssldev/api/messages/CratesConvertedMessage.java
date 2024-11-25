package org.ssldev.api.messages;

import java.io.File;
import java.util.List;

import org.ssldev.api.chunks.Ptrk;
import org.ssldev.api.chunks.Tvcn;
import org.ssldev.core.messages.Message;

/**
 * parent crate and its children was converted
 */
public class CratesConvertedMessage extends Message {
	public final File parentCrateFile;
	public final String parentCrateName;
	public final List<Tvcn> parentdisplayColumns; 
	public final List<Ptrk> ptrks; 

	public CratesConvertedMessage (File parentCrateFile, String parentCrateName, List<Tvcn> parentDisplayColumns, List<Ptrk> ptrks) {
		this.parentCrateFile = parentCrateFile;
		this.parentCrateName = parentCrateName;
		this.parentdisplayColumns = parentDisplayColumns;
		this.ptrks = ptrks;
	}
}
