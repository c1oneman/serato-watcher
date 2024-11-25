package org.ssldev.api.web;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import org.ssldev.api.messages.TrackStatusMessage;
import org.ssldev.core.messages.MessageIF;
import org.ssldev.core.services.ServiceIF;
import org.ssldev.core.utils.Logger;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TrackStatusWebSocketHandler extends TextWebSocketHandler implements ServiceIF {
    
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private volatile boolean isInitialized = false;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Logger.info(this, "New WebSocket connection established: " + session.getId());
        sessions.add(session);
        isInitialized = true;
        try {
            // Send initial status message
            session.sendMessage(new TextMessage("{\"status\":\"CONNECTED\",\"message\":\"WebSocket connection established\",\"sessionId\":\"" + session.getId() + "\"}"));
            Logger.info(this, "Active sessions after connection: " + sessions.size());
        } catch (IOException e) {
            Logger.error(this, "Error sending connection confirmation: " + e.getMessage());
        }
    }

    @Override
    public void notify(MessageIF msg) {
        if (!isInitialized) {
            Logger.warn(this, "WebSocket handler not yet initialized, skipping message");
            return;
        }

        Logger.info(this, "TrackStatusWebSocketHandler received message: " + msg.getClass().getSimpleName());
        
        if (msg instanceof TrackStatusMessage) {
            TrackStatusMessage status = (TrackStatusMessage) msg;
            String json = convertToJson(status);
            Logger.info(this, "Broadcasting track status to " + sessions.size() + " sessions: " + json);
            
            if (sessions.isEmpty()) {
                Logger.warn(this, "No WebSocket sessions connected to receive update. Handler initialized: " + isInitialized);
                return;
            }
            
            for (WebSocketSession session : sessions) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(json));
                        Logger.info(this, "Successfully sent message to session: " + session.getId());
                    } else {
                        Logger.info(this, "Removing closed session: " + session.getId());
                        sessions.remove(session);
                    }
                } catch (Exception e) {
                    Logger.error(this, "Error sending message to session " + session.getId() + ": " + e.getMessage());
                    Logger.error(this, "Stack trace: " + e);
                    sessions.remove(session);
                }
            }
            Logger.info(this, "Finished broadcasting to all sessions. Active sessions: " + sessions.size());
        } else {
            Logger.debug(this, "Ignoring non-TrackStatusMessage: " + msg.getClass().getSimpleName());
        }
    }

    private String convertToJson(TrackStatusMessage msg) {
        return String.format(
            "{\"status\":\"%s\",\"deck\":%d,\"volume\":%.2f,\"track\":{\"title\":\"%s\",\"artist\":\"%s\",\"bpm\":%d}}",
            msg.getStatus(),
            msg.getDeck(),
            msg.getVolume(),
            msg.getTrack().getTitle(),
            msg.getTrack().getArtist(),
            msg.getTrack().getBpm()
        );
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        Logger.error(this, "Transport error on session " + session.getId() + ": " + exception.getMessage());
        sessions.remove(session);
        Logger.info(this, "Active sessions after error: " + sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Logger.info(this, "WebSocket connection closed: " + session.getId() + " with status: " + status);
        sessions.remove(session);
        Logger.info(this, "Active sessions after close: " + sessions.size());
    }

    private void broadcastMessage(String message) {
        if (sessions.isEmpty()) {
            Logger.warn(this, "No WebSocket sessions connected to receive update");
            return;
        }
        
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                    Logger.info(this, "Successfully sent message to session: " + session.getId());
                } else {
                    Logger.info(this, "Removing closed session: " + session.getId());
                    sessions.remove(session);
                }
            } catch (Exception e) {
                Logger.error(this, "Error sending message to session " + session.getId() + ": " + e.getMessage());
                sessions.remove(session);
            }
        }
    }
} 