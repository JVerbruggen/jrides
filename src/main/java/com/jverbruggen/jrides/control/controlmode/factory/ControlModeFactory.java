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

package com.jverbruggen.jrides.control.controlmode.factory;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controlmode.*;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;

import java.util.List;

public class ControlModeFactory {
    public ControlMode getForWithOperator(RideHandle rideHandle){
        if(rideHandle == null) return null;

        List<StationHandle> stationHandles = rideHandle.getStationHandles();
        int size = stationHandles.size();
        if(size == 0)
            return null;
        else if(size == 1)
            return getSingleForWithOperator(rideHandle, stationHandles.get(0));
        else{
            throw new RuntimeException("No support for multiple stations yet for control mode ( with operator )");
        }
    }

    public ControlMode getForWithoutOperator(RideHandle rideHandle){
        if(rideHandle == null) return null;

        List<StationHandle> stationHandles = rideHandle.getStationHandles();
        int size = stationHandles.size();
        if(size == 0)
            return new VoidMode();
        else if(size == 1)
            return createSimpleAutomaticMode(rideHandle, stationHandles.get(0));
        else{
            // For dispatching 1 station at a time
            return getAlternateForWithoutOperator(rideHandle, stationHandles);
        }
    }

    private ControlMode getSingleForWithOperator(RideHandle rideHandle, StationHandle stationHandle){
        if(stationHandle == null) return null;
        MinMaxWaitingTimer waitingTimer = stationHandle.getWaitingTimer();

        return new SemiAutomaticMode(
                rideHandle,
                stationHandle.getTriggerContext(),
                waitingTimer);
    }

    private ControlMode createSimpleAutomaticMode(RideHandle rideHandle, StationHandle stationHandle){
        if(stationHandle == null) return null;
        MinMaxWaitingTimer waitingTimer = stationHandle.getWaitingTimer();

        return new AutomaticMode(
                rideHandle,
                stationHandle.getTriggerContext(),
                waitingTimer);
    }

    private ControlMode getAlternateForWithoutOperator(RideHandle rideHandle, List<StationHandle> stationHandles){
        ControlMode automaticMode = createSimpleAutomaticMode(rideHandle, stationHandles.get(0));

        return new AutomaticAlternateMode(automaticMode, stationHandles);
    }
}
