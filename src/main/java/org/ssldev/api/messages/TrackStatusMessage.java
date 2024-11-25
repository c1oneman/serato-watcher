package org.ssldev.api.messages;

import org.ssldev.api.chunks.AdatC;
import org.ssldev.core.messages.MessageIF;

public class TrackStatusMessage implements MessageIF {
    public enum Status {
        STARTED,
        STOPPED,
        VOLUME_CHANGED
    }

    private final AdatC track;
    private final Status status;
    private final int deck;
    private final double volume;

    public TrackStatusMessage(AdatC track, Status status, int deck, double volume) {
        this.track = track;
        this.status = status;
        this.deck = deck;
        this.volume = volume;
    }

    public AdatC getTrack() { return track; }
    public Status getStatus() { return status; }
    public int getDeck() { return deck; }
    public double getVolume() { return volume; }
} 