package com.jverbruggen.jrides.control.controller;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.config.coaster.objects.ControllerConfig;
import com.jverbruggen.jrides.config.coaster.objects.controller.DualControllerSpecConfig;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.exception.CoasterLoadException;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;

public class RideControllerFactory {
    public RideController createCoasterController(CoasterHandle coasterHandle, ControllerConfig controllerConfig) throws CoasterLoadException {
        if(!coasterHandle.hasStation())
            return createVoidCoasterController(coasterHandle);

        if(controllerConfig == null || controllerConfig.getType().equalsIgnoreCase(ControllerConfig.CONTROLLER_DEFAULT))
            return createDefaultCoasterController(coasterHandle);

        String type = controllerConfig.getType();
        if(type.equalsIgnoreCase(ControllerConfig.CONTROLLER_ALTERNATE))
            return createDualCoasterController(coasterHandle, controllerConfig.getAlternateControllerSpecConfig());
        if(type.equalsIgnoreCase(ControllerConfig.CONTROLLER_SIMULTANEOUS))
            return createDualCoasterController(coasterHandle, controllerConfig.getAlternateControllerSpecConfig());

        throw new RuntimeException("Ride controller type " + type + " is not supported");
    }

    private RideController createDualCoasterController(CoasterHandle coasterHandle, DualControllerSpecConfig dualControllerSpecConfig) {
        String stationLeftName = dualControllerSpecConfig.getStationLeft();
        String stationRightName = dualControllerSpecConfig.getStationRight();

        CoasterStationHandle stationHandleLeft = coasterHandle.getStationHandle(stationLeftName);
        CoasterStationHandle stationHandleRight = coasterHandle.getStationHandle(stationRightName);

        if(stationHandleLeft == null) throw new RuntimeException("Could not find station " + stationLeftName + " to make ride controller");
        else if(stationHandleRight == null) throw new RuntimeException("Could not find station " + stationRightName + " to make ride controller");

        return new DualRideController(stationHandleLeft.getTriggerContext(), stationHandleRight.getTriggerContext(), coasterHandle);
    }

    private RideController createDefaultCoasterController(CoasterHandle coasterHandle) throws CoasterLoadException {
        CoasterStationHandle stationHandle = coasterHandle.getStationHandle(0);
        if(stationHandle == null){
            throw new CoasterLoadException("Ride controller for " + coasterHandle.getRide().getIdentifier() + " could not be created because no station was available");
        }

        return new SimpleRideController(coasterHandle, stationHandle.getTriggerContext());
    }

    private RideController createVoidCoasterController(CoasterHandle coasterHandle){
        return SimpleRideController.createVoid(coasterHandle);

    }

    public RideController createFlatRideController(FlatRideHandle flatRideHandle){
        return new SimpleRideController(flatRideHandle, flatRideHandle.getFirstTriggerContext());
    }
}
