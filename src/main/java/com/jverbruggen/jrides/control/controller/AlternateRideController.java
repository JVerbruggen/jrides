package com.jverbruggen.jrides.control.controller;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controller.base.SingularRideController;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class AlternateRideController extends SingularRideController implements RideController {
    private TriggerContext leftStationTriggerContext;
    private TriggerContext rightStationTriggerContext;

    public AlternateRideController(TriggerContext leftStationTriggerContext, TriggerContext rightStationTriggerContext) {
        this.leftStationTriggerContext = leftStationTriggerContext;
        this.rightStationTriggerContext = rightStationTriggerContext;
    }

    @Override
    public void setRideHandle(RideHandle rideHandle) {
        throw new RuntimeException("Cannot change ride handle for alternate ride controller");
    }

    @Override
    public void changeMode(ControlMode newControlMode) {

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


    public TriggerContext getLeftStationTriggerContext() {
        return leftStationTriggerContext;
    }

    public TriggerContext getRightStationTriggerContext() {
        return rightStationTriggerContext;
    }
}
