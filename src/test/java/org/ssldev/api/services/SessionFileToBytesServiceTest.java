package org.ssldev.api.services;

import static org.junit.Assert.assertEquals;
import static org.ssldev.api.TestConfig.TEST_SESSION_FILES_LOC;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.ssldev.api.TestLastMsgReceivedService;
import org.ssldev.api.chunks.Adat;
import org.ssldev.api.chunks.Vrsn;
import org.ssldev.api.messages.LiveSessionBytesMessage;
import org.ssldev.api.messages.SessionFileCreatedMessage;
import org.ssldev.api.messages.SessionFileModifiedMessage;
import org.ssldev.api.utils.DataExtractUtil;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.utils.Logger;

public class SessionFileToBytesServiceTest {

	SessionDirectoryFilesToBytesService service;
	private TestLastMsgReceivedService testService = new TestLastMsgReceivedService();
	private EventHub hub;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		Logger.enableDebug(true);
		
		hub = new EventHub();
		hub.register(testService, LiveSessionBytesMessage.class);
		service = new SessionDirectoryFilesToBytesService(hub);
	}
	
	@Test
	public void sessionFile_created() {
		File session = new File(TEST_SESSION_FILES_LOC + "1_song.session");
		hub.add(new SessionFileCreatedMessage(session));
		
		LiveSessionBytesMessage result = getExpectedResult();
		
		Vrsn vrsn =  DataExtractUtil.extractVrsn(result.byteConsumer);
		assertEquals("1.0/Serato Scratch LIVE Review", vrsn.getData());
		
		List<Adat> adats =  DataExtractUtil.extractAdat(result.byteConsumer);
		assertEquals(1, adats.size());
		Adat adat = adats.get(0);

		AdatChecker.build(adat)
				   .checkRow(600)
				   .checkFullPath("/Users/elad/Desktop/Music/Music_from_gray_hd/Party In Central.mp3")
				   .checkArtist("Krafty Kuts ")
				   .checkTitle("Party In Central (funky inst)")
				   .checkAlbum("Tricka Technology")
				   .checkGenre("funky downtempo")
				   .checkBpm(99)
				   .checkComment("underground")
				   .checkStartTime(1534021905)
				   .checkEndTime(1534021945)
				   .checkTotalPlayTime(40)
				   .checkUpdateTime(1534021945)
				   .checkDeck(2)
				   .checkIsPlayed(true);
	}
	@Test
	public void sessionFile_modified() {
		File session = new File(TEST_SESSION_FILES_LOC + "1_song.session");
		hub.add(new SessionFileModifiedMessage(session));
		
		LiveSessionBytesMessage result = getExpectedResult();
		
		Vrsn vrsn =  DataExtractUtil.extractVrsn(result.byteConsumer);
		assertEquals("1.0/Serato Scratch LIVE Review", vrsn.getData());
		
		List<Adat> adats =  DataExtractUtil.extractAdat(result.byteConsumer);
		assertEquals(1, adats.size());
		Adat adat = adats.get(0);
		
		AdatChecker.build(adat)
		.checkRow(600)
		.checkFullPath("/Users/elad/Desktop/Music/Music_from_gray_hd/Party In Central.mp3")
		.checkArtist("Krafty Kuts ")
		.checkTitle("Party In Central (funky inst)")
		.checkAlbum("Tricka Technology")
		.checkGenre("funky downtempo")
		.checkBpm(99)
		.checkComment("underground")
		.checkStartTime(1534021905)
		.checkEndTime(1534021945)
		.checkTotalPlayTime(40)
		.checkUpdateTime(1534021945)
		.checkDeck(2)
		.checkIsPlayed(true);
	}
	
	@Test
	public void sessionFile_Windows() {
		File session = new File(TEST_SESSION_FILES_LOC + "win_reg.session");
		hub.add(new SessionFileModifiedMessage(session));
		
		LiveSessionBytesMessage result = getExpectedResult();
		
		Vrsn vrsn =  DataExtractUtil.extractVrsn(result.byteConsumer);
		assertEquals("1.0/Serato Scratch LIVE Review", vrsn.getData());
		
		List<Adat> adats =  DataExtractUtil.extractAdat(result.byteConsumer);
		assertEquals(2, adats.size());
		
		AdatChecker.build( adats.get(0))
		.checkRow(8)
		.checkFullPath("C:\\Users\\pasca\\Documents\\2019-E04\\Blaikz - Summer Love (Hands Up Freaks Remix).mp3")
		.checkTitle("Blaikz - Summer Love (Hands Up Freaks Remix)")
		.checkBpm(70)
		.checkSessionId(6)
		.checkStartTime(1548537285)
		.checkEndTime(1548537510)
		.checkTotalPlayTime(225)
		.checkUpdateTime(1548537510)
		.checkDeck(1)
		.checkKey("F#m")
		.checkIsPlayed(true);
		
		AdatChecker.build(adats.get(1))
		.checkRow(9)
		.checkFullPath("C:\\Users\\pasca\\Documents\\2019-E04\\Gainworx - Voices Of Sahara (Extended Mix).mp3")
		.checkTitle("Gainworx - Voices Of Sahara (Extended Mix)")
		.checkBpm(138)
		.checkSessionId(6)
		.checkStartTime(1548537416)
		.checkEndTime(1548537510)
		.checkTotalPlayTime(94)
		.checkUpdateTime(1548537510)
		.checkDeck(2)
		.checkKey("Bm")
		.checkIsPlayed(true);
	}
	
	@Test
	public void sessionFile_Windows_Offline() {
		File session = new File(TEST_SESSION_FILES_LOC + "win_offline.session");
		hub.add(new SessionFileModifiedMessage(session));
		
		LiveSessionBytesMessage result = getExpectedResult();
		
		Vrsn vrsn =  DataExtractUtil.extractVrsn(result.byteConsumer);
		assertEquals("1.0/Serato Scratch LIVE Review", vrsn.getData());
		
		List<Adat> adats =  DataExtractUtil.extractAdat(result.byteConsumer);
		assertEquals(2, adats.size());
		
		AdatChecker.build( adats.get(0))
		.checkRow(3)
		.checkFullPath("C:\\Users\\pasca\\Documents\\2019-E04\\Blaikz - Summer Love (Hands Up Freaks Remix).mp3")
		.checkTitle("Blaikz - Summer Love (Hands Up Freaks Remix)")
		.checkBpm(0)
		.checkSessionId(1)
		.checkDeck(1)
		.checkIsPlayed(true)
		.checkplayerType("Offline Player");
		
		AdatChecker.build(adats.get(1))
		.checkRow(4)
		.checkFullPath("C:\\Users\\pasca\\Documents\\2019-E04\\Gainworx - Voices Of Sahara (Extended Mix).mp3")
		.checkTitle("Gainworx - Voices Of Sahara (Extended Mix)")
		.checkBpm(0)
		.checkSessionId(1)
		.checkDeck(2)
		.checkIsPlayed(true)
		.checkplayerType("Offline Player");
	}
	
	@Test public void process_session_file_with_unknown_chunks() {
		Logger.enableDebug(true);
		File session = new File(TEST_SESSION_FILES_LOC + "nick.session");
		hub.add(new SessionFileModifiedMessage(session));
		
		LiveSessionBytesMessage result = getExpectedResult();
		
		List<Adat> adats =  DataExtractUtil.extractAdat(result.byteConsumer);
		
		assertEquals(966, adats.size());
	}
	
	private LiveSessionBytesMessage getExpectedResult() {
		return testService.lastMsgRecvd == null ? null : (LiveSessionBytesMessage) testService.lastMsgRecvd;
	}
	

}
