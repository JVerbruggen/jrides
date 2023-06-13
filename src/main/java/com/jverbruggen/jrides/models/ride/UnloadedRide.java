package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.properties.PlayerLocation;

public class UnloadedRide implements Ride {
    private String identifier;

    public UnloadedRide(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public PlayerLocation getWarpLocation() {
        return null;
    }

    @Override
    public boolean isLoaded() {
        return false;
    }
}
