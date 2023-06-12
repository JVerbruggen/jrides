package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;

import javax.annotation.Nonnull;
import java.util.List;

public interface RideHandle {
    void tick();
    Ride getRide();
    RideController getRideController();
    Menu getRideControlMenu();

    void setRideController(RideController rideController, Menu rideControlMenu);
    DispatchTrigger getDispatchTrigger();
    TriggerContext getTriggerContext(@Nonnull String contextOwner);
    TriggerContext getFirstTriggerContext();

    PlayerLocation getEjectLocation();
    List<StationHandle> getStationHandles();

    List<RideCounterRecordCollection> getTopRideCounters();
    List<Player> getPassengers();

    void setState(RideState state);
    RideState getState();
    boolean canEnter(Player player);

    void open(Player authority);
    void close(Player authority);
    boolean canFullyClose();
}
