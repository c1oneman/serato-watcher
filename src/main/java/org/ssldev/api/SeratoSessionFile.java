package org.ssldev.api;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.toList;
import static org.ssldev.core.utils.StringUtils.toDateAndTime;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.ssldev.api.chunks.Adat;
import org.ssldev.api.chunks.AdatC;
import org.ssldev.core.utils.Validate;

/**
 * represents a serato session file.
 */
public class SeratoSessionFile implements Serializable
{
	private static final long serialVersionUID = 5646346228273407130L;
	
	public final String sessionFileName;
	public final String  sessionFileAbsolutePath;
	public final long lastModifiedTime;
	public final long sessionStarttime;
	public final long sessionEndTime;
	public final List<AdatC> adatsInSessionOrderedByStartTime;
	
	public SeratoSessionFile(File sessionFile, List<Adat> adatsInSession) 
	{
		this(sessionFile.getName(), sessionFile.getAbsolutePath(), sessionFile.lastModified(), adatsInSession);
	}
	
	public SeratoSessionFile(String sessionFileName, String sessionFileAbsolutePath, 
			long lastModifiedTime, List<Adat> adatsInSession) 
	{
		Validate.notNull(sessionFileName, "session file name cannot be null");
		Validate.notNull(sessionFileAbsolutePath, "session file absolute path cannot be null");
		Validate.notNull(adatsInSession, "tracks in session file cannot be null");
		Validate.isTrue(lastModifiedTime > 0, "last modified time has to be greater than zero");
		Objects.requireNonNull(adatsInSession).forEach(Objects::requireNonNull);
		
		this.sessionFileName = sessionFileName;
		this.sessionFileAbsolutePath =  sessionFileAbsolutePath;
		this.lastModifiedTime = lastModifiedTime;
		this.adatsInSessionOrderedByStartTime = consumerAndOrderByStartTime(adatsInSession);
		
		sessionStarttime = getFirstAdatStartTimeOrDefault(adatsInSessionOrderedByStartTime, 0);
		sessionEndTime = getLastAdatEndTimeOrDefault(adatsInSessionOrderedByStartTime, 0);
	}
	
	
	private static long getLastAdatEndTimeOrDefault(List<AdatC> adatsOrderedByStartTime, long defaultValue) {
		
		for(int i = adatsOrderedByStartTime.size() - 1; i >= 0 ; --i) {
			AdatC a = adatsOrderedByStartTime.get(i);
			if(a.endTime > 0) {
				return a.endTime;
			}
		}
		
		return defaultValue;
	}
	private static long getFirstAdatStartTimeOrDefault(List<AdatC> adatsOrderedByStartTime, long defaultValue) {
		
		for(AdatC a : adatsOrderedByStartTime) {
			if(a.startTime > 0) {
				return a.startTime;
			}
		}
		
		return defaultValue;
	}

	/**
	 * converts the given list to a list of {@link AdatC} and orders them 
	 * by start time (earlier to later).  
	 * @param adats to consume
	 * @return ordered list of {@link AdatC} by start time
	 */
	public static List<AdatC> consumerAndOrderByStartTime(Collection<Adat> adats) {
		return adats.stream()
				.map(AdatC::new)
				.sorted(Comparator.comparingLong(a -> a.startTime))
				.collect(toList());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("session #:").append(sessionFileName).append(lineSeparator());
		sb.append("  ").append("Start time         : [ ").append(toDateAndTime(sessionStarttime)).append(" ]").append(lineSeparator());
		sb.append("  ").append("End time           : [ ").append(toDateAndTime(sessionEndTime)).append(" ]").append(lineSeparator());
		sb.append("  ").append("# tracks in session: [ ").append(adatsInSessionOrderedByStartTime.size()).append(" ]").append(lineSeparator());
		
		return super.toString();
	}
}
