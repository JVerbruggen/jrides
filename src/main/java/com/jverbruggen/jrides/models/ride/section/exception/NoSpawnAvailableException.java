package com.jverbruggen.jrides.models.ride.section.exception;

import com.jverbruggen.jrides.models.ride.coaster.Track;

public class NoSpawnAvailableException extends RuntimeException {
    private final Track track;

    public NoSpawnAvailableException(Track track) {
        this.track = track;
    }

    @Override
    public String getMessage() {
        return "No spawn section available on track " + this.track.toString() + " for train to spawn!";
    }
}
