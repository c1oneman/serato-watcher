package org.ssldev.api.app;
// Test commit 2 3
import java.util.Objects;

import org.ssldev.api.services.NowPlayingGuiService;
import org.ssldev.api.services.SessionBytesPublisherService;
import org.ssldev.api.services.SessionDirectoryFilesToBytesService;
import org.ssldev.api.services.SslCurrentSessionFinderService;
import org.ssldev.api.services.SslSessionFileUnmarshalService;
import org.ssldev.api.services.TrackPublisherService;
import org.ssldev.core.messages.MessageIF;
import org.ssldev.core.mgmt.AsyncEventHub;
import org.ssldev.core.mgmt.EventHub;
import org.ssldev.core.services.ServiceIF;
import org.ssldev.core.services.SystemInfoValidatorService;
import org.ssldev.core.utils.Logger;
import org.ssldev.core.utils.Validate;
import org.ssldev.api.web.TrackStatusController;
import org.ssldev.api.web.TrackStatusWebSocketHandler;
import org.ssldev.api.messages.TrackStatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SslApi {

	private String pathToSeratoFolder = null;
	private static String configFileName = "sslApiConfiguration.properties";
	private static boolean showGui = false;
	
	private EventHub hub; 

	@Autowired(required = false)
	private TrackStatusWebSocketHandler webSocketHandler;

	public static void main(String[] args) {
		SslApi m = new SslApi(new AsyncEventHub(3));

		m.start();
	}
	
	public SslApi() {
		
		setConfigurationProps();
		
		// current threading model requires 3 threads (more threads may cause out-of-order notifications; current implementation does not guarantee order)
		// 1 event thread for GUI
		// 1 SSL directory/file change listener
		// 1 general worker thread to service the blocking event queue
		int numThreads = showGui? 3 : 2;

		// defaultify the event hub if none is provided
		this.hub = new AsyncEventHub(numThreads);
		
		registerAllServices(hub);
	}
	
	public SslApi(EventHub hub) {
		this.hub = Objects.requireNonNull(hub);
		
		setConfigurationProps();
		registerAllServices(hub);
	}
	
	private void setConfigurationProps() {
		// load the configuration
		SslApiConfig.load(configFileName);

		showGui = SslApiConfig.getBooleanProperty(SslApiConfig.Property.START_GUI);

		// setup the logger
		Logger.enableDebug(SslApiConfig.getBooleanProperty(SslApiConfig.Property.ENABLE_DEBUG)); 
		Logger.enableFinest(SslApiConfig.getBooleanProperty(SslApiConfig.Property.ENABLE_FINEST)); 
		Logger.enableTrace(SslApiConfig.getBooleanProperty(SslApiConfig.Property.ENABLE_TRACE));
		Logger.setShowTime(SslApiConfig.getBooleanProperty(SslApiConfig.Property.SHOW_TIME));

		Logger.info(this, "Running with the following properties:" + System.lineSeparator() + SslApiConfig.instance.toString());

		if(SslApiConfig.getBooleanProperty(SslApiConfig.Property.ENABLE_LOG_TO_FILE)) {
			String logFilePath = SslApiConfig.getStringProperty(SslApiConfig.Property.LOG_FILE_PATH);
			Logger.init(logFilePath);
		}

		pathToSeratoFolder = SslApiConfig.getStringProperty(SslApiConfig.Property.SSL_DIR_PATH);
	}
	
	
	/**
	 * initializes and starts the API.  This call is identical to initializing and
	 * starting the underlying {@link EventHub}.
	 */
	public void start() {
		hub.init();
		hub.start();
	}
	
	/**
	 * allow shutdown of API
	 */
	public void shutdown() {
		hub.shutdown();
	}
	
	@SuppressWarnings("unused")
	private void registerAllServices(EventHub hub) {
		Logger.info(this, "Registering all API services:");
		
		// Register WebSocket handler if available
		if (webSocketHandler != null) {
			Logger.info(this, "Registering WebSocket handler with EventHub");
			hub.register(webSocketHandler, TrackStatusMessage.class);
		} else {
			Logger.warn(this, "WebSocket handler not available during service registration");
		}
		
		// Register REST controller
		TrackStatusController trackStatusController = new TrackStatusController();
		Logger.info(this, "Registering Track Status Controller");
		hub.register(trackStatusController, TrackStatusMessage.class);
		
		// optional service to provide a GUI display for what songs currently loaded/playing
		if(showGui) {
			NowPlayingGuiService gui = new NowPlayingGuiService(hub);
		}
		
		// create all services 
		Logger.info(this, "Registering core services");
		
		// for sys info output
		SystemInfoValidatorService sysInfoService = new SystemInfoValidatorService(hub);
		// for live session monitoring:
		SessionDirectoryFilesToBytesService sslFileToBytesService = new SessionDirectoryFilesToBytesService(hub);
		SessionBytesPublisherService dataPubService = new SessionBytesPublisherService(hub);
		TrackPublisherService trackPublisher = new TrackPublisherService(hub);
		
		// new up last since it activates on construction
		SslCurrentSessionFinderService sslFileService = new SslCurrentSessionFinderService(hub, pathToSeratoFolder);
		
		// provides API for converting whole session files into bytes
		SslSessionFileUnmarshalService sslUnmarshalService = new SslSessionFileUnmarshalService(hub);
	}
	
	/**
	 * register this service to be notified of given message/event set.  If no messages are given, the service
	 * will be notified of all messages/events.
	 * 
	 * @param externalService to notify
	 * @param msgs optional set of messags to be notified of
	 */
	@SafeVarargs
	public final void register(ServiceIF externalService, Class<? extends MessageIF>... msgs){
		Validate.areClassesAssignableFrom(msgs, MessageIF.class);
		
		hub.register(externalService, msgs);
	}
	
	public void setWebSocketHandler(TrackStatusWebSocketHandler handler) {
		this.webSocketHandler = handler;
		if (hub != null && handler != null) {
			Logger.info(this, "Registering WebSocket handler with hub");
			hub.register(handler, TrackStatusMessage.class);
		}
	}
}
