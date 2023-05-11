package com.jverbruggen.jrides.control.controller;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controller.base.SingularRideController;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.controlmode.factory.ControlModeFactory;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class SimpleRideController extends SingularRideController implements RideController {
    private final CoasterStationHandle stationHandle;

    public SimpleRideController(CoasterStationHandle stationHandle, RideHandle rideHandle) {
        super();
        this.stationHandle = stationHandle;
        this.changeMode(this.getControlModeFactory().getForWithoutOperator(rideHandle));

        this.setRideHandle(rideHandle);
    }
}
