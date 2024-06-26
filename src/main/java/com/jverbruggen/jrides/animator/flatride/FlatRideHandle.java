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

package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.AbstractRideHandle;
import com.jverbruggen.jrides.animator.flatride.seat.SeatComponent;
import com.jverbruggen.jrides.animator.flatride.station.FlatRideStationHandle;
import com.jverbruggen.jrides.animator.flatride.timing.TimingSequence;
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.config.ride.RideCounterMapConfigs;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.SimpleDispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.event.player.PlayerFinishedRideEvent;
import com.jverbruggen.jrides.event.ride.RideFinishedEvent;
import com.jverbruggen.jrides.models.entity.Passenger;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecord;
import com.jverbruggen.jrides.models.ride.gate.Gate;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

public class FlatRideHandle extends AbstractRideHandle {
    private TimingSequence timingSequence;
    private final FlatRideStationHandle stationHandle;
    private final SimpleDispatchTrigger dispatchTrigger;
    private final List<SeatComponent> seatComponents;

    private FlatRideState flatRideState;

    public FlatRideHandle(World world, Ride ride, boolean loaded, FlatRideStationHandle stationHandle, SoundsConfig sounds, PlayerLocation customEjectLocation, RideCounterMapConfigs rideCounterMapConfigs) {
        super(world, ride, null, loaded, sounds, customEjectLocation, rideCounterMapConfigs);
        this.stationHandle = stationHandle;
        this.timingSequence = null;
        this.dispatchTrigger = stationHandle.getTriggerContext().getDispatchTrigger();
        this.flatRideState = FlatRideState.IDLE;
        this.seatComponents = new ArrayList<>();

        stationHandle.setFlatRideHandle(this);
        stationHandle.getTriggerContext().getRestraintTrigger().getLock().addEventListener(c -> this.onRestraintLockUpdateEventListener(c.isUnlocked()));
        stationHandle.getEntryGates().forEach(Gate::open);
    }

    @Override
    public void tick() {
        if(!isLoaded()) return;

        RideController rideController = getRideController();
        if(rideController.isActive())
            rideController.getControlMode().tick();

        switch(this.flatRideState){
            case IDLE -> {
                vehicleTick();

                boolean dispatchActive = dispatchTrigger.isActive();
                if(dispatchActive){
                    boolean dispatched = checkForDispatch(dispatchActive);
                    if(dispatched){
                        stationHandle.runExitEffectTriggers(null);
                        this.flatRideState = FlatRideState.STARTING;
                    }
                }
            }
            case STARTING -> {
                if(stationHandle.exitEffectTriggersDone()){
                    this.flatRideState = FlatRideState.RUNNING;
                }
            }
            case RUNNING -> {
                boolean finished = vehicleTick();

                if(finished) {
                    stationHandle.runEntryEffectTriggers(null);
                    this.flatRideState = FlatRideState.STOPPING;
                }
            }
            case STOPPING -> {
                if(stationHandle.entryEffectTriggersDone()){
                    onRideFinish();
                    this.flatRideState = FlatRideState.IDLE;
                }
            }
        }
    }

    private boolean vehicleTick(){
        boolean finished = this.timingSequence.tick();
        this.stationHandle.getVehicle().tick();

        return finished;
    }

    private boolean checkForDispatch(boolean dispatchActive){
        if(dispatchActive && this.timingSequence.isIdle()){
            onRideStart();

            Vehicle vehicle = getVehicle();
            getRideController().onVehicleDepart(vehicle, stationHandle);
            vehicle.playDispatchSound();
            return true;
        }else return false;
    }

    private void onRideStart(){
        dispatchTrigger.reset();
        this.timingSequence.restart();
        stationHandle.getTriggerContext().getVehiclePresentLock().setLocked(true);
        stationHandle.getEntryGates().forEach(Gate::close);
    }

    private void onRideFinish(){
        PlayerFinishedRideEvent.sendFinishedRideEvent(getPassengers()
                .stream()
                .map(p -> (JRidesPlayer)p.getPlayer())
                .collect(Collectors.toList()), getRide());
        RideFinishedEvent.send(getRide(), getPassengers().stream().map(Passenger::getPlayer).toList());

        stationHandle.getTriggerContext().getVehiclePresentLock().setLocked(false);
        stationHandle.getTriggerContext().getRestraintTrigger().getLock().setLocked(true);
        stationHandle.getVehicle().ejectPassengers();
        stationHandle.getEntryGates().forEach(Gate::open);
    }

    private void onRestraintLockUpdateEventListener(boolean unlocked){
        seatComponents.forEach(c -> c.setRestraint(unlocked));
    }

    public void setTimingSequence(TimingSequence timingSequence) {
        this.timingSequence = timingSequence;
    }

    public void addRootComponent(FlatRideComponent component){
        stationHandle.getVehicle().addRootComponent(component);
    }

    public List<FlatRideComponent> getRootComponents() {
        return stationHandle.getVehicle().getRootComponents();
    }

    @Override
    public DispatchTrigger getDispatchTrigger() {
        return getFirstTriggerContext().getDispatchTrigger();
    }

    @Override
    public TriggerContext getTriggerContext(@Nonnull String contextOwner) {
        return getFirstTriggerContext();
    }

    @Override
    public TriggerContext getFirstTriggerContext() {
        return stationHandle.getTriggerContext();
    }

    @Override
    public PlayerLocation getEjectLocation() {
        return stationHandle.getEjectLocation();
    }

    @Override
    public List<StationHandle> getStationHandles() {
        return List.of(stationHandle);
    }

    @Override
    public List<RideCounterRecord> getTopRideCounters() {
        return null;
    }

    @Override
    public List<Passenger> getPassengers() {
        return stationHandle.getVehicle().getPassengers();
    }

    public Vehicle getVehicle(){
        return stationHandle.getVehicle();
    }

    public void addSeatComponent(SeatComponent seatComponent){
        this.seatComponents.add(seatComponent);
    }
}

enum FlatRideState{
    IDLE,
    STARTING,
    RUNNING,
    STOPPING
}