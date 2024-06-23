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


import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;

import java.util.List;

public class AutomaticAlternateMode implements ControlMode{
    private int stationHandlePointer;
    private final List<StationHandle> stationHandles;
    private final ControlMode automaticMode;

    public AutomaticAlternateMode(ControlMode automaticMode, List<StationHandle> stationHandles) {
        this.stationHandlePointer = 0;
        this.stationHandles = stationHandles;
        this.automaticMode = automaticMode;
    }

    private void incrementStationHandlePointer(){
        stationHandlePointer = (stationHandlePointer+1) % stationHandles.size();

        TriggerContext triggerContext = stationHandles.get(stationHandlePointer).getTriggerContext();
        automaticMode.setTriggerContext(triggerContext);
    }

    @Override
    public void tick() {
        automaticMode.tick();
    }

    @Override
    public void onVehicleArrive(Vehicle vehicle, StationHandle stationHandle) {
        automaticMode.onVehicleArrive(vehicle, stationHandle);
    }

    @Override
    public void onVehicleDepart(Vehicle vehicle, StationHandle stationHandle) {
        automaticMode.onVehicleDepart(vehicle, stationHandle);
        incrementStationHandlePointer();
    }

    @Override
    public boolean setOperator(Player player) {
        return automaticMode.setOperator(player);
    }

    @Override
    public boolean allowsAction(ControlAction action, Player player) {
        return automaticMode.allowsAction(action, player);
    }

    @Override
    public MinMaxWaitingTimer getWaitingTimer() {
        return automaticMode.getWaitingTimer();
    }

    @Override
    public Player getOperator() {
        return automaticMode.getOperator();
    }

    @Override
    public void setTriggerContext(TriggerContext triggerContext) {
//        throw new RuntimeException("Not implemented");
        automaticMode.setTriggerContext(triggerContext);
        JRidesPlugin.getLogger().warning("Set trigger context on alternate mode (unhandled)");
    }
}
