package org.ssldev.api.chunks;

import java.io.Serializable;
import java.util.Objects;

/**
 * a partially consumed {@link Adat}
 */
public class AdatC implements Serializable {
	private static final long serialVersionUID = -7958836713225581498L;
	
	public final String fullPath;
	public final String title;
	public final String artist;
	public final String key;
	public final int sessionId;
	public final int bpm;
	public final long startTime;
	public final long endTime;
	public final long totalPlayTime;
	
	public AdatC(Adat a) {
		fullPath = a.getFullPath();
		title = a.getTitle();
		artist = a.getArtist();
		key = a.getKey();
		sessionId = a.getSessionId();
		bpm = a.getBpm();
		startTime = a.getStartTime();
		endTime = a.getEndTime();
		totalPlayTime = a.getTotalPlayTime();
	}
	
	public static AdatC from(Adat a) {
		return new AdatC(a);
	}
	
	public String getFullPath() { return fullPath; }
	public String getTitle() { return title; }
	public String getArtist() { return artist; }
	public String getKey() { return key; }
	public int getSessionId() { return sessionId; }
	public int getBpm() { return bpm; }
	public long getStartTime() { return startTime; }
	public long getEndTime() { return endTime; }
	public long getTotalPlayTime() { return totalPlayTime; }
	
	
	@Override
	public String toString() {
		return artist + " - "+title;
	}
	
	@Override
		public int hashCode() {
			return Objects.hash(artist, title);
		}
	
	@Override
		public boolean equals(Object o) {
			if(o == this) return true;
			if(!(o instanceof AdatC)) return false;
			
			AdatC other = (AdatC) o;
			
			return Objects.equals(artist, other.artist) && 
				   Objects.equals(title, other.title)
				   ;
		}
}
