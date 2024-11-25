package org.ssldev.api.services;

import static java.lang.System.lineSeparator;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ssldev.api.SeratoSessionFile;
import org.ssldev.api.messages.LoadSeratoSessionFilesMessage;
import org.ssldev.api.messages.SeratoSessionsLoadCompleteMessge;
import org.ssldev.api.messages.SessionFileUnmarshalError;
import org.ssldev.api.messages.SessionFileUnmarshalledMessage;
import org.ssldev.api.messages.SessionFilesLoadedFromDisk;
import org.ssldev.api.messages.UnmarshalMultipleSessionsRequest;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.Service;
import org.ssldev.core.utils.Logger;
import org.ssldev.core.utils.SaveUtil;
import org.ssldev.core.utils.StopWatch;
import org.ssldev.core.utils.Timer;

/**
 * <p>
 * loads the Serato session files as requested by the {@link LoadSeratoSessionFilesMessage}.
 * Loading up of sessions is expensive and time consuming, thus already 
 * loaded sessions should be serialized for future speedup.  
 * <p>
 * Certain use cases should be noted and considered:<br>
 * 
 * 1) we want to ignore practice sessions or listening sessions as much as
 * possible, as they may not really reflect "statistically" worthy tracks. Thus 
 * sessions with less than a certain number of tracks should be ignored.<br>
 * 
 * 2) we may want to limit how old session file are so that non-relevant tracks
 * aren't included
 * <p>
 * Consumes: 
 * <ul>
 * <li>{@link LoadSeratoSessionFilesMessage}</li>
 * <li>{@link SessionFileUnmarshalledMessage}</li>
 * <li>{@link SessionFileUnmarshalError}</li>
 * </ul>
 * Produces:
 * <ul>
 * <li>{@link UnmarshalMultipleSessionsRequest}</li>
 * <li>{@link SessionFilesLoadedFromDisk}</li>
 * <li>{@link SeratoSessionsLoadCompleteMessge}</li>
 * </ul>
 */
public class SeratoSessionsLoaderService extends Service {
	/** used to get execution time of session file loading */
	private StopWatch watch;
	
	/** map of loaded session files by session name */
	private HashMap<String, SeratoSessionFile> loadedSessionFiles = new HashMap<String, SeratoSessionFile>();
	
	/** key used to save and load this service data */
	private static final String SAVE_KEY = SeratoSessionsLoaderService.class.getSimpleName();

	/** set of files awaiting unmarshalling, by session name */
	private Set<String> awaitingLoadConfirmationsFor = new HashSet<>();

	private Runnable interruptTimer = () ->{};
	
	private static final int INTERRUPT_TIME_IN_SECONDS = 10;

	private long lastTimeSessionFileWasUnmarshalledInSeconds;
	
	
	public SeratoSessionsLoaderService(EventHub hub) {
		super(hub);
	}

	@Override
	public void init() {
		loadSessionsFromDisk();
		hub.register(LoadSeratoSessionFilesMessage.class, this::onLoadRequest);
		hub.register(SessionFileUnmarshalledMessage.class, this::onFileUnmarshalled);
		hub.register(SessionFileUnmarshalError.class, this::onFileUnmarshallError);
	}
	
	@Override
	public void start() {
		if(!loadedSessionFiles.isEmpty()) {
			hub.add(new SessionFilesLoadedFromDisk(loadedSessionFiles));
		}
	}


	private void onLoadRequest(LoadSeratoSessionFilesMessage msg) 
	{
		List<File> sessionsToLoad = msg.sessionFilesThatShouldBeLoaded;
		Logger.info(this, "received request to load ["+sessionsToLoad.size()+"] session files");
		
		if(!awaitingLoadConfirmationsFor.isEmpty()) {
			Logger.error(this, "Serato Sessions loader is still in process of completing a previous request.  ignoring last received.");
			return;
		}
		
		// filter out sessions already loaded
		sessionsToLoad = sessionsToLoad.stream().filter(sess -> !isAlreadyLoaded(sess)).collect(toList());
		if(sessionsToLoad.isEmpty()) {
			Logger.info(this, "Session files already loaded. No need to unmarshal any new files. (current # session files: ["+loadedSessionFiles.size()+"])");
			notifyLoadComplete();
		}
		else {
			// interrupt session loading if not complete in given time period
			interruptTimer = Timer.doEvery(this::interruptSessionLoadingIfStuck, 15, SECONDS);
			
			requestSessionFilesUnmarshal(sessionsToLoad);
		}
	}
	

	private void requestSessionFilesUnmarshal(List<File> sessionsToLoad) {
		watch = new StopWatch().start();
		
		awaitingLoadConfirmationsFor = new HashSet<>(toListOfFileNames(sessionsToLoad));
		
		Logger.info(this, "Sending requests to unmarshal ["+sessionsToLoad.size()+"] session files");
		
		hub.add(new UnmarshalMultipleSessionsRequest(sessionsToLoad));
	}

	private void onFileUnmarshalled(SessionFileUnmarshalledMessage msg) {
		
		lastTimeSessionFileWasUnmarshalledInSeconds = watch.elapsed(SECONDS);
		
		SeratoSessionFile session = msg.seratoSession;
		
		if(!awaitingLoadConfirmationsFor.contains(session.sessionFileName)) {
			Logger.warn(this, "unexpected session file got unmarshalled: ["+session.sessionFileName+"]");
		}
		else {
			awaitingLoadConfirmationsFor.remove(session.sessionFileName);
			
			
			loadedSessionFiles.put(session.sessionFileName, session);
			Logger.info(this, "  ["+session.sessionFileName+"] unmarshalled ("
									+loadedSessionFiles.size()+" loaded, "+awaitingLoadConfirmationsFor.size() +" more to go)...");
			
			if(awaitingLoadConfirmationsFor.isEmpty()) {
				onLoadComlete();
			}
		}
	}
	
	private void onFileUnmarshallError(SessionFileUnmarshalError msg) {
		lastTimeSessionFileWasUnmarshalledInSeconds = watch.elapsed(SECONDS);
		
		String failedSessionName = msg.sessionFile.getName();
		
		if(!awaitingLoadConfirmationsFor.contains(failedSessionName)) {
			Logger.warn(this, "unexpected session file error: ["+failedSessionName+"]");
		}
		else {
			awaitingLoadConfirmationsFor.remove(failedSessionName);
			
			Logger.error(this, "  ["+failedSessionName+"] failed to unmarshal ["+msg.errorMsg+"]\n("
									+loadedSessionFiles.size()+" loaded, "+awaitingLoadConfirmationsFor.size() +" more to go)");
			
			if(awaitingLoadConfirmationsFor.isEmpty()) {
				onLoadComlete();
			}
		}
	}
	
	private void onLoadComlete() {
		// cancel interrupt timer.  load is complete
		cancelSessionLoadingInterruptTimer();
		
		Logger.info(this, "completed loading ["+loadedSessionFiles.size()+"] serato session files ( "+watch.stop()+" )");
		
		saveSessionsToDisk();
		
		notifyLoadComplete();
	}
	
	private void notifyLoadComplete() {
		hub.add(new SeratoSessionsLoadCompleteMessge(loadedSessionFiles, getClass()));
	}

	@SuppressWarnings("unchecked")
	private void loadSessionsFromDisk() {
		Logger.info(this, "loading previously loaded Serato sessions...");
		
		try {
			
			List<Serializable> load = SaveUtil.getInstance().load(SAVE_KEY);
			if (null == load || load.isEmpty()) {
				Logger.info(this, "no saved data found for " + SAVE_KEY);
				return;
			}
			
			if(load.size() < 1) {
				Logger.info(this, "not enough data found for " +SAVE_KEY);
				return;
			}
			
			loadedSessionFiles = (HashMap<String, SeratoSessionFile>) load.get(0);
			Logger.debug(this, "loaded "+ loadedSessionFiles.size() + " files..");
			loadedSessionFiles.keySet().forEach(v -> Logger.debug(this, "  " + v));
			
		} catch (Throwable e) {
			Logger.error(this, "encountered an error attempting to load previously saved session files."
					+ "  Deleting file so it doesnt happen again.");
			SaveUtil.getInstance().delete(SAVE_KEY);
		}
		
	}
	// save files for faster startup time.
	private void saveSessionsToDisk() {
		Logger.info(this, "saving data for " + SAVE_KEY);
		SaveUtil.getInstance().save(SAVE_KEY, loadedSessionFiles);
	}
	
	private boolean isAlreadyLoaded(File f) {
		if(!loadedSessionFiles.containsKey(f.getName())) {
			Logger.debug(this, "does not contain " +f.getName());
		}
		return loadedSessionFiles.containsKey(f.getName());
	}
	
	private void interruptSessionLoading() {
		if(!awaitingLoadConfirmationsFor.isEmpty()) {
			
			Logger.error(this, "session file loading halted for more than ["+INTERRUPT_TIME_IN_SECONDS +"] seconds. "
					+ "["+awaitingLoadConfirmationsFor.size()+"] files did not get loaded. proceeding with ["+
					loadedSessionFiles.size()+"] loaded files."+lineSeparator()+"session files not loaded:"+lineSeparator()
					+asList(awaitingLoadConfirmationsFor));

			awaitingLoadConfirmationsFor.clear();
			// kick off load complete with whatever ws managed to be unmarshalled (also cancels periodic timer)
			onLoadComlete();
		}
	}
	
	private void interruptSessionLoadingIfStuck() {
		if(lastTimeSessionFileWasUnmarshalledInSeconds == 0) {
			// no files were yet unmarshalled.. keep waiting
		}
		else if(watch.elapsed(SECONDS) - lastTimeSessionFileWasUnmarshalledInSeconds > INTERRUPT_TIME_IN_SECONDS) {
			interruptSessionLoading();
		}
	}

	private void cancelSessionLoadingInterruptTimer() {
		interruptTimer.run();
	}
	
	private static List<String> toListOfFileNames(List<File> files) {
		return files.stream().map(File::getName).collect(toList());
	}
	private static String asList(Set<String> values) {
		return values.stream().collect(joining(lineSeparator()));
	}

}
