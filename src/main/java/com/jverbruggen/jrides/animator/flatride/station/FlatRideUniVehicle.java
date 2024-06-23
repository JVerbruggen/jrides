/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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

    public FlatRideUniVehicle(String name, boolean debugMode, DispatchLock restraintLock, DispatchLock vehiclePresentLock) {
        super(name, debugMode);
        this.rootComponents = new ArrayList<>();
        this.onStation = true;
        this.restraintLock = restraintLock;

        vehiclePresentLock.addEventListener(l -> setStationary(l.isUnlocked()));
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

    public void setStationary(boolean stationary){
        onStation = stationary;
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
