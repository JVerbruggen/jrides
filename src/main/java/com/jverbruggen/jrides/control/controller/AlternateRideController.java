package com.jverbruggen.jrides.control.controller;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controller.base.BaseRideController;
import com.jverbruggen.jrides.control.controller.base.SingularRideController;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class AlternateRideController extends SingularRideController implements RideController {
    private RideHandle rideHandle;
    private RideController innerControllerA;
    private RideController innerControllerB;

    public AlternateRideController(RideHandle rideHandle, RideController innerControllerA, RideController innerControllerB) {
        this.rideHandle = rideHandle;
        this.innerControllerA = innerControllerA;
        this.innerControllerB = innerControllerB;
    }

    @Override
    public void setRideHandle(RideHandle rideHandle) {
        throw new RuntimeException("Cannot change ride handle for alternate ride controller");
    }

    @Override
    public TriggerContext getTriggerContext() {
        return rideHandle.getTriggerContext(null);
    }

    @Override
    public void changeMode(ControlMode newControlMode) {

    }

    @Override
    public Ride getRide() {
        return rideHandle.getRide();
    }

    @Override
    public void onTrainArrive(Train train, StationHandle stationHandle) {

    }

    @Override
    public void onTrainDepart(Train train, StationHandle stationHandle) {

    }

    @Override
    public boolean setOperator(Player player) {
        return false;
    }

    @Override
    public Player getOperator() {
        return null;
    }


}
