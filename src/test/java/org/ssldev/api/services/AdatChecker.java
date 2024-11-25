package org.ssldev.api.services;

import static org.junit.Assert.assertEquals;

import org.ssldev.api.chunks.Adat;

/**
 * used to build an ADAT matcher that verifies ADAT data
 * matches expected values 
 */
public class AdatChecker {
	Adat adatToCheck;
	
	private AdatChecker(Adat adatToCheck) {
		this.adatToCheck = adatToCheck;
	}

	public static AdatChecker build(Adat adatToCheck) {
		return new AdatChecker(adatToCheck);
	}
	
	public AdatChecker checkAlbum(String val) {
		assertEquals(val, adatToCheck.getAlbum()); return this;
	}
	public AdatChecker checkArtist(String val) {
		assertEquals(val, adatToCheck.getArtist()); return this;
	}
	public AdatChecker checkBpm(int val) {
		assertEquals(val, adatToCheck.getBpm()); return this;
	}
	public AdatChecker checkComment(String val) {
		assertEquals(val, adatToCheck.getComment()); return this;
	}
	public AdatChecker checkDeck(int val) {
		assertEquals(val, adatToCheck.getDeck()); return this;
	}
	public AdatChecker checkEndTime(long val) {
		assertEquals(val, adatToCheck.getEndTime()); return this;
	}
	public AdatChecker checkStartTime(long val) {
		assertEquals(val, adatToCheck.getStartTime()); return this;
	}
	public AdatChecker checkUpdateTime(long val) {
		assertEquals(val, adatToCheck.getUpdateTime()); return this;
	}
	public AdatChecker checkTotalPlayTime(long val) {
		assertEquals(val, adatToCheck.getTotalPlayTime()); return this;
	}
	public AdatChecker checkFullPath(String val) {
		assertEquals(val, adatToCheck.getFullPath()); return this;
	}
	public AdatChecker checkGenre(String val) {
		assertEquals(val, adatToCheck.getGenre()); return this;
	}
	public AdatChecker checkKey(String val) {
		assertEquals(val, adatToCheck.getKey()); return this;
	}
	public AdatChecker checkRow(int val) {
		assertEquals(val, adatToCheck.getRow()); return this;
	}
	public AdatChecker checkTitle(String val) {
		assertEquals(val, adatToCheck.getTitle()); return this;
	}
	public AdatChecker checkIsPlayed(boolean val) {
		assertEquals(val, adatToCheck.isPlayed()); return this;
	}
	public AdatChecker checkSessionId(int val) {
		assertEquals(val, adatToCheck.getSessionId()); return this;
	}
	public AdatChecker checkplayerType(String val) {
		assertEquals(val, adatToCheck.getPlayerType()); return this;
	}
	
}
