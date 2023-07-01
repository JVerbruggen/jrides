package com.jverbruggen.jrides.animator.flatride.station;

import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;
import com.jverbruggen.jrides.models.ride.gate.Gate;

import java.util.List;

public class FlatRideStationHandle extends StationHandle {
    private FlatRideHandle flatRideHandle;
    private final FlatRideUniVehicle vehicle;

    public FlatRideStationHandle(String name, String shortName, List<Gate> entryGates, PlayerLocation ejectLocation, MinMaxWaitingTimer waitingTimer, TriggerContext triggerContext) {
        super(name, shortName, entryGates, ejectLocation, waitingTimer, triggerContext);
        this.vehicle = new FlatRideUniVehicle(name + "_vehicle", false,
                triggerContext.getRestraintTrigger().getLock(), triggerContext.getVehiclePresentLock());
    }

    @Override
    public Vehicle getStationaryVehicle() {
        if(vehicle.isStationary()) return vehicle;
        return null;
    }

    public FlatRideUniVehicle getVehicle() {
        return vehicle;
    }

    public void setFlatRideHandle(FlatRideHandle flatRideHandle) {
        this.flatRideHandle = flatRideHandle;
        this.vehicle.setFlatRideHandle(flatRideHandle);
    }
}
