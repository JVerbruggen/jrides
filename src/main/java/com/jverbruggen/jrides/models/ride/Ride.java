package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.ride.coaster.StartTrigger;

public interface Ride {
    String getIdentifier();
    StartTrigger getStartTrigger();
}
