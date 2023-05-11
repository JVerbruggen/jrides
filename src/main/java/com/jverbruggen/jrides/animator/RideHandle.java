package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenu;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;

import java.util.List;

public interface RideHandle {
    void tick();
    Ride getRide();
    RideController getRideController();
    RideControlMenu getRideControlMenu();

    void setRideController(RideController rideController, RideControlMenu rideControlMenu);
    DispatchTrigger getDispatchTrigger();
    TriggerContext getTriggerContext(String contextOwner);

    PlayerLocation getEjectLocation();
    List<StationHandle> getStationHandles();
}
