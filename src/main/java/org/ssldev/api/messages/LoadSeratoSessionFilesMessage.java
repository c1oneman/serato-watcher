package org.ssldev.api.messages;

import java.io.File;
import java.util.List;
import java.util.Objects;

import org.ssldev.core.messages.Message;
import org.ssldev.core.messages.MessageIF;

/**
 * a request to load (i.e. read in) a list of serato session files.
 */
public class LoadSeratoSessionFilesMessage extends Message implements MessageIF {

	public final List<File> sessionFilesThatShouldBeLoaded;

	public LoadSeratoSessionFilesMessage(List<File> sessionFilesThatShouldBeLoaded) 
	{
		Objects.requireNonNull(sessionFilesThatShouldBeLoaded)
			   .forEach(Objects::requireNonNull);
		
		this.sessionFilesThatShouldBeLoaded = sessionFilesThatShouldBeLoaded;
	}

}
