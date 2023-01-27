package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenu;
import com.jverbruggen.jrides.models.ride.Ride;

public interface RideHandle {
    void start();

    Ride getRide();

    RideController getRideController();
    RideControlMenu getRideControlMenu();

    void setRideController(RideController rideController);

    DispatchTrigger getDispatchTrigger();

    TriggerContext getTriggerContext(String contextOwner);
}
