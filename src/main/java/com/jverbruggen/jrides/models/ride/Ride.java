package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;

import java.util.List;

public interface Ride {
    String getIdentifier();
    String getDisplayName();
    List<RideCounterRecordCollection> getTopRideCounters();

    PlayerLocation getWarpLocation();
}
