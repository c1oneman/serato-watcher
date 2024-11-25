package org.ssldev.api.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.ssldev.api.messages.TrackStatusMessage;
import org.ssldev.api.messages.TrackStatusMessage.Status;
import org.ssldev.core.messages.MessageIF;
import org.ssldev.core.services.ServiceIF;
import org.ssldev.core.utils.Logger;

@RestController
@CrossOrigin
public class TrackStatusController implements ServiceIF {
    
    private volatile TrackStatusMessage lastStatus;

    @GetMapping("/api/tracks/status")
    public String getStatus() {
        Logger.info(this, "GET /api/tracks/status called, lastStatus: " + (lastStatus != null ? "present" : "null"));
        
        if (lastStatus == null) {
            return "{\"status\":\"NO_TRACK\"}";
        }
        String response = convertToJson(lastStatus);
        Logger.info(this, "Returning status: " + response);
        return response;
    }

    @GetMapping("/api/websocket/test")
    public String testWebSocket() {
        Logger.info(this, "Testing WebSocket broadcast");
        if (lastStatus != null) {
            notify(lastStatus);
            return "{\"status\":\"TEST_SENT\",\"track\":\"" + lastStatus.getTrack().getTitle() + "\"}";
        }
        return "{\"status\":\"NO_TRACK_TO_TEST\"}";
    }

    @Override
    public void notify(MessageIF msg) {
        Logger.info(this, "REST Controller received message: " + msg.getClass().getSimpleName());
        
        if (msg instanceof TrackStatusMessage) {
            lastStatus = (TrackStatusMessage) msg;
            Logger.info(this, "Updated last status: " + convertToJson(lastStatus));
        }
    }

    private String convertToJson(TrackStatusMessage msg) {
        return String.format(
            "{\"status\":\"%s\",\"deck\":%d,\"volume\":%.2f,\"track\":{\"title\":\"%s\",\"artist\":\"%s\"}}",
            msg.getStatus(),
            msg.getDeck(),
            msg.getVolume(),
            msg.getTrack().getTitle(),
            msg.getTrack().getArtist()
        );
    }
} 