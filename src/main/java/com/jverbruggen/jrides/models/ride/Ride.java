package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.math.Vector3;

public interface Ride {
    String getIdentifier();
    String getDisplayName();

    Vector3 playerWarpLocation();

}
