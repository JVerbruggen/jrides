package com.jverbruggen.jrides.control.controller;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.config.coaster.objects.ControllerConfig;
import com.jverbruggen.jrides.config.coaster.objects.controller.AlternateControllerSpecConfig;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;

public class RideControllerFactory {
    public RideController createRideController(CoasterHandle coasterHandle, ControllerConfig controllerConfig){
        if(controllerConfig == null || controllerConfig.getType().equalsIgnoreCase(ControllerConfig.CONTROLLER_DEFAULT))
            return createDefaultRideController(coasterHandle);

        String type = controllerConfig.getType();
        if(type.equalsIgnoreCase(ControllerConfig.CONTROLLER_ALTERNATE))
            return createAlternateRideController(coasterHandle, controllerConfig.getAlternateControllerSpecConfig());

        throw new RuntimeException("Ride controller type " + type + " is not supported");
    }

    private RideController createAlternateRideController(CoasterHandle coasterHandle, AlternateControllerSpecConfig alternateControllerSpecConfig) {
        String stationLeftName = alternateControllerSpecConfig.getStationLeft();
        String stationRightName = alternateControllerSpecConfig.getStationRight();

        CoasterStationHandle stationHandleLeft = coasterHandle.getStationHandle(stationLeftName);
        CoasterStationHandle stationHandleRight = coasterHandle.getStationHandle(stationRightName);

        if(stationHandleLeft == null) throw new RuntimeException("Could not find station " + stationLeftName + " to make ride controller");
        else if(stationHandleRight == null) throw new RuntimeException("Could not find station " + stationRightName + " to make ride controller");

        return new AlternateRideController(stationHandleLeft.getTriggerContext(), stationHandleRight.getTriggerContext(), coasterHandle);
    }

    private RideController createDefaultRideController(CoasterHandle coasterHandle){
        CoasterStationHandle stationHandle = coasterHandle.getStationHandle(0);

        return new SimpleRideController(stationHandle, coasterHandle);
    }
}
