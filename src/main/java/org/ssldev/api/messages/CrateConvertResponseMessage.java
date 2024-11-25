package org.ssldev.api.messages;

import java.io.File;
import java.util.List;
import java.util.Objects;

import org.ssldev.core.messages.Message;
import org.ssldev.core.utils.Validate;
/**
 * serato crate contents after conversion
 */
public class CrateConvertResponseMessage extends Message {
	public File crateFile;
	public String crateName;
	public List<String> displayedColumns;
	public List<String> crateTracksPaths;

	public CrateConvertResponseMessage(File crateFile, String crateName, List<String> displayedColumns, List<String> crateTracksPaths) {
		Validate.exists(crateFile);
		Objects.requireNonNull(displayedColumns, "displayed columns cannot be null");
		Objects.requireNonNull(crateTracksPaths, "crate track paths cannot be null");
		Objects.requireNonNull(crateName, "crate name cannot be null");
		
		this.crateFile = crateFile;
		this.crateName = crateName;
		this.displayedColumns = displayedColumns;
		this.crateTracksPaths = crateTracksPaths;
	}

}
