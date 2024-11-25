package org.ssldev.api.services;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.ssldev.core.utils.StringUtils.toDateAndTime;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.ssldev.api.messages.LoadSeratoSessionFilesMessage;
import org.ssldev.api.messages.LocateValidSeratoSessionFilesMessage;
import org.ssldev.api.messages.SeratoSessionsLoadErrorMessage;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;
import org.ssldev.core.utils.Logger;

/**
 * looks for valid Serato history session files that should be loaded, based
 * on criteria provided via incoming {@link LocateValidSeratoSessionFilesMessage} 
 * message.<p>
 * 
 * Loading up of sessions 
 * is expensive and time consuming, thus state should be serialized for 
 * future speedup, but also pruned to prevent staleness of data.<p>
 * 
 * Certain use cases should be noted and considered:<p>
 * 
 * 1) we want to ignore practice sessions or listening sessions as much as
 * possible, as they may not really reflect "statistically" worthy tracks. Thus 
 * sessions with less than a certain number of tracks should be ignored.<p>
 * 
 * 2) we may want to limit how old session file are so that non-relevant tracks
 * aren't included<p>
 * 
 * Consumes: 
 * <ul>
 * <li>{@link LocateValidSeratoSessionFilesMessage}</li>
 * </ul>
 * Produces:
 * <ul>
 * <li>{@link LoadSeratoSessionFilesMessage}</li>
 * </ul>
 */
public class HistoricalSessionsLocatorService extends Service {
	private int maxAgeInMonths;
	private int maxFileSizeInBytes;
	private int minFileSizeInBytes;
	private File sessionsFileDirLoc;
	
	/** SSL session file */
	private static final String SESSION_POSTFIX = ".session";
	
	/** today's date */
	private final static LocalDate today = LocalDate.now();

	
	public HistoricalSessionsLocatorService(EventHub hub) {
		super(hub);
	}
	
	@Override
	public void init() {
		super.init();
		
		hub.register(LocateValidSeratoSessionFilesMessage.class, this::onLocate);
	}

	
	private void onLocate(LocateValidSeratoSessionFilesMessage msg) {
		setCriteria(msg);
		
		Logger.info(this, "locating valid session files per the following criteria:"+System.lineSeparator()
						+criterias()
						);
		
		List<File> sessionFilesThatShouldBeLoaded = findSessionFiles(); 
		
		if(sessionFilesThatShouldBeLoaded.size() > 0) {
			hub.add( new LoadSeratoSessionFilesMessage( sessionFilesThatShouldBeLoaded ) );
		}
		else {
			Logger.error(this, "could not find any valid session files in ["+sessionsFileDirLoc+"] that matched criterias");
			hub.add( new SeratoSessionsLoadErrorMessage("could not find any valid session files in ["+sessionsFileDirLoc+"]") );
		}
	}

	private void setCriteria(LocateValidSeratoSessionFilesMessage msg) {
		this.maxAgeInMonths = msg.maxAgeInMonths;
		this.maxFileSizeInBytes = msg.maxFileSizeInBytes;
		this.minFileSizeInBytes = msg.minFileSizeInBytes;
		this.sessionsFileDirLoc = msg.sessionsFileDirLoc;
	}
	

	private List<File> findSessionFiles() {
		// search for "meaningful" session files 
		// (pass the validity criterias; e.g. not too old or too small)
		List<File> sessList = Arrays.asList(sessionsFileDirLoc.listFiles()).stream()
									.filter(s -> isSessionValid(s).equals(Reason.IS_VALID))
									.sorted(Comparator.comparingLong(File::lastModified).reversed())
									.collect(toList());
		
		Logger.info(this, "Found ["+ sessList.size()+"] valid session files...");
		Logger.debug(this, System.lineSeparator() + toStr(sessList));
		
		return sessList;
	}
	
	private String toStr(List<File> sessList) {
		return sessList.stream()
				.map(f ->  f.getName() + " : " + toDateAndTime(f.lastModified()))
				.collect(joining(lineSeparator(),"",""));
	}
	private String criterias() {
		StringBuilder sb = new StringBuilder();
		sb.append("  ").append("Session file dir:         ").append("[ ").append(sessionsFileDirLoc.getAbsolutePath()).append(" ]").append(lineSeparator());
		sb.append("  ").append("Max age (in months):      ").append("[ ").append(maxAgeInMonths).append(" ]").append(lineSeparator());
		sb.append("  ").append("Max file size (in bytes): ").append("[ ").append(maxFileSizeInBytes).append(" ]").append(lineSeparator());
		sb.append("  ").append("Min file size (in bytes): ").append("[ ").append(minFileSizeInBytes).append(" ]").append(lineSeparator());
		return sb.toString();
	}

	private static enum Reason {
		IS_VALID, TOO_OLD, TOO_SMALL, TOO_BIG, INVALID_FILE_TYPE
	}
	private Reason isSessionValid(File s) {
		LocalDate sesionDate = Instant.ofEpochMilli(s.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate();
		;
		long diffInMonths = ChronoUnit.MONTHS.between(sesionDate, today);

		// check rules:
		// is it a session file? .session
		if (!s.getName().endsWith(SESSION_POSTFIX))
			return Reason.INVALID_FILE_TYPE;
		// too old?
		if (isSizeValid(maxAgeInMonths) &&  diffInMonths > maxAgeInMonths)
			return Reason.TOO_OLD;
		// too small?
		if (isSizeValid(minFileSizeInBytes) && s.length() <minFileSizeInBytes)
			return Reason.TOO_SMALL;
		if (isSizeValid(maxFileSizeInBytes) && s.length() > maxFileSizeInBytes)
			return Reason.TOO_BIG;

		return Reason.IS_VALID;
	}

	private boolean isSizeValid(int s) {
		return s >= 0;
	}
	
}
