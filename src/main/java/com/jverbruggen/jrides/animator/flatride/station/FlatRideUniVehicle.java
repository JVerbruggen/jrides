package com.jverbruggen.jrides.animator.flatride.station;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.coaster.train.AbstractVehicle;

import java.util.ArrayList;
import java.util.List;

public class FlatRideUniVehicle extends AbstractVehicle {
    private final List<FlatRideComponent> rootComponents;
    private FlatRideHandle flatRideHandle;
    private boolean onStation;
    private final DispatchLock restraintLock;

    public FlatRideUniVehicle(String name, boolean debugMode, DispatchLock restraintLock) {
        super(name, debugMode);
        this.rootComponents = new ArrayList<>();
        this.onStation = true;
        this.restraintLock = restraintLock;
    }

    public void tick(){
        this.rootComponents.forEach(FlatRideComponent::tick);
    }


    public void addRootComponent(FlatRideComponent component){
        rootComponents.add(component);
    }

    public List<FlatRideComponent> getRootComponents() {
        return rootComponents;
    }

    @Override
    public boolean isStationary() {
        return onStation;
    }

    @Override
    public boolean getRestraintState() {
        return restraintLock.isUnlocked();
    }

    @Override
    public void setRestraintForAll(boolean closed) {
        restraintLock.setLocked(!closed);
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
        return flatRideHandle.getRootComponents().get(0).getPosition();
    }

    public void setFlatRideHandle(FlatRideHandle flatRideHandle) {
        this.flatRideHandle = flatRideHandle;
    }
}
