package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.properties.PlayerLocation;

public interface Ride {
    String getIdentifier();
    String getDisplayName();

    PlayerLocation getWarpLocation();
    boolean isLoaded();
}
