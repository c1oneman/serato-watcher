package org.ssldev.api.messages;

import java.io.File;
import java.util.List;

import org.ssldev.api.chunks.Ptrk;
import org.ssldev.api.chunks.Tvcn;
import org.ssldev.core.messages.Message;
/**
 * crate name and contents 
 */
public class CrateConvertedMessage extends Message {
	public final File crateFile;
	public final String crateName;
	public final List<Ptrk> ptrks;
	public final List<Tvcn> displayedColumns;

	public CrateConvertedMessage(File crateFile, String crateName, List<Tvcn> displayedColumns, List<Ptrk> ptrks) {
		this.crateFile = crateFile;
		this.crateName = crateName;
		this.displayedColumns = displayedColumns;
		this.ptrks = ptrks;
	}

}
