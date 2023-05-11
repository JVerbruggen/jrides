package com.jverbruggen.jrides.control.controller;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.config.coaster.objects.ControllerConfig;
import com.jverbruggen.jrides.config.coaster.objects.controller.AlternateControllerSpecConfig;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;

public class RideControllerFactory {
    public RideController createRideController(CoasterHandle coasterHandle, ControllerConfig controllerConfig){
        if(controllerConfig == null || controllerConfig.getType().equalsIgnoreCase("default"))
            return createDefaultRideController(coasterHandle);

        String type = controllerConfig.getType();
        if(type.equalsIgnoreCase("alternate"))
            return createAlternateRideController(coasterHandle, controllerConfig.getAlternateControllerSpecConfig());

        throw new RuntimeException("Ride controller type " + type + " is not supported");
    }

    private RideController createAlternateRideController(CoasterHandle coasterHandle, AlternateControllerSpecConfig alternateControllerSpecConfig) {
        String stationLeftName = alternateControllerSpecConfig.getStationLeft();
        String stationRightName = alternateControllerSpecConfig.getStationRight();

        CoasterStationHandle stationHandleLeft = coasterHandle.getStationHandle(stationLeftName);
        CoasterStationHandle stationHandleRight = coasterHandle.getStationHandle(stationRightName);

        RideController innerControllerLeft = new SimpleRideController(stationHandleLeft, coasterHandle);
        RideController innerControllerRight = new SimpleRideController(stationHandleRight, coasterHandle);

        throw new RuntimeException("Not implemented yet alternate ride controller");
    }

    private RideController createDefaultRideController(CoasterHandle coasterHandle){
        CoasterStationHandle stationHandle = coasterHandle.getStationHandle(0);

        return new SimpleRideController(stationHandle, coasterHandle);
    }
}
