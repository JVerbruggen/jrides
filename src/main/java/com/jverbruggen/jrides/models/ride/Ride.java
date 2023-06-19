package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.api.JRidesRide;
import com.jverbruggen.jrides.models.properties.PlayerLocation;

public interface Ride extends JRidesRide {
    String getIdentifier();
    String getDisplayName();

    PlayerLocation getWarpLocation();
    boolean isLoaded();
    boolean canExitDuringRide();
}
