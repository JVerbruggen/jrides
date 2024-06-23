///************************************************************************************************************
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

package com.jverbruggen.jrides.control.controlmode.advanced;
//
//import com.jverbruggen.jrides.animator.RideHandle;
//import com.jverbruggen.jrides.control.controlmode.BaseControlMode;
//import com.jverbruggen.jrides.control.controlmode.ControlMode;
//import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
//import com.jverbruggen.jrides.models.ride.StationHandle;
//import com.jverbruggen.jrides.models.ride.coaster.train.Train;
//
//import java.util.List;
//
//public class ProxyControlMode extends BaseControlMode implements ControlMode {
////    private List<ControlMode> targets;
////    private List<StationHandle> stationHandles;
////
////    public ProxyControlMode(RideHandle rideHandle, MinMaxWaitingTimer waitingTimer, List<StationHandle> stationHandles) {
////        super(rideHandle, null,
////                waitingTimer,
////                null,
////                true);
////
////        this.stationHandles = stationHandles;
////    }
////
////    @Override
////    protected void incrementWaitingTimer() {
////        stationHandles.forEach(s -> s.getWaitingTimer().increment(tickInterval));
////    }
////
////    @Override
////    public StationHandle getStationHandle() {
////        throw new RuntimeException("Cannot get station handle in proxy control mode");
////    }
////
////    @Override
////    public void onVehicleArrive(Train train, StationHandle stationHandle) {
////        targets.stream().filter(t -> t.getStationHandle() == stationHandle)
////                .findFirst()
////                .orElseThrow(() -> new RuntimeException("Station handle not found on vehicle arrive!"))
////                .onVehicleArrive(train, stationHandle);
////    }
////
////    @Override
////    public void onVehicleDepart(Train train, StationHandle stationHandle) {
////        targets.stream().filter(t -> t.getStationHandle() == stationHandle)
////                .findFirst()
////                .orElseThrow(() -> new RuntimeException("Station handle not found on vehicle depart!"))
////                .onVehicleDepart(train, stationHandle);
////    }
//}
