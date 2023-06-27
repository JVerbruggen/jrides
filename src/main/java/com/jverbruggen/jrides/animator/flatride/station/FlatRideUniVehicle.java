package com.jverbruggen.jrides.animator.flatride.station;

import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.coaster.train.AbstractVehicle;

import java.util.ArrayList;

public class FlatRideUniVehicle extends AbstractVehicle {
    private FlatRideHandle flatRideHandle;
    private boolean onStation;

    public FlatRideUniVehicle(String name, boolean debugMode) {
        super(name, debugMode);
        this.onStation = true;
    }

    @Override
    public boolean isStationary() {
        return onStation;
    }

    @Override
    public boolean getRestraintState() {
        return false;
    }

    @Override
    public void setRestraintForAll(boolean locked) {

    }

    @Override
    public void ejectPassengers() {
        PlayerLocation ejectLocation = flatRideHandle.getEjectLocation();
        for(Player passenger : new ArrayList<>(getPassengers())){
            passenger.teleport(ejectLocation, true);
        }
    }

    @Override
    public void playRestraintOpenSound() {

    }

    @Override
    public void playRestraintCloseSound() {

    }

    @Override
    public void playDispatchSound() {

    }

    @Override
    public Vector3 getCurrentLocation() {
        return null;
    }

    public void setFlatRideHandle(FlatRideHandle flatRideHandle) {
        this.flatRideHandle = flatRideHandle;
    }
}
