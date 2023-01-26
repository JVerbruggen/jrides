package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.models.ride.Ride;

public interface RideHandle {
    void start();

    Ride getRide();

    RideController getRideController();

    DispatchTrigger getDispatchTrigger();
}
