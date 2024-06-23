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

package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.DebounceCall;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;

public class AutomaticMode extends BaseControlMode implements ControlMode{
    private final DebounceCall dispatchDebounce;

    public AutomaticMode(RideHandle rideHandle, TriggerContext triggerContext, MinMaxWaitingTimer waitingTimer) {
        super(rideHandle, triggerContext, waitingTimer);

        this.dispatchDebounce = new DebounceCall(20);
    }

    @Override
    public void tick() {
        super.tick();

        stationTick();
    }

    void stationTick(){
        if(!rideHandle.isOpen()) return;

        MinMaxWaitingTimer waitingTimer = getWaitingTimer();

        StationHandle stationHandle = triggerContext.getParentStation();
        Vehicle stationaryVehicle = stationHandle.getStationaryVehicle();
        if(stationaryVehicle == null) return;

        int visualTime = waitingTimer.getVisualDispatchTime(waitingTimer.timeUntilPreferredWaitingTime());
        waitingTimer.sendTimeWaitingNotification(stationaryVehicle.getPassengers(), visualTime);

        if(!waitingTimer.reachedFunction()) return;
        triggerContext.getGateTrigger().setGatesState(true);
        triggerContext.getRestraintTrigger().setRestraintsState(true);

        DispatchLockCollection dispatchLockCollection = triggerContext.getDispatchLockCollection();
        if(dispatchLockCollection != null && !dispatchLockCollection.allUnlocked()) return;

        dispatchDebounce.run(() -> triggerContext.getDispatchTrigger().execute(null));
    }

    @Override
    public void onVehicleArrive(Vehicle vehicle, StationHandle stationHandle) {
        super.onVehicleArrive(vehicle, stationHandle);

        dispatchDebounce.reset();
        stationHandle.openEntryGates();
    }

    @Override
    public void onVehicleDepart(Vehicle vehicle, StationHandle stationHandle) {
        super.onVehicleDepart(vehicle, stationHandle);

        dispatchDebounce.reset();
        stationHandle.closeEntryGates();
    }

    @Override
    public boolean setOperator(Player player) {
        return false;
    }

    @Override
    public boolean allowsAction(ControlAction action, Player player) {
        return false;
    }

    @Override
    public Player getOperator() {
        return null;
    }
}
