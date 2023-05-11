package com.jverbruggen.jrides.control.controller;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public interface RideController {
    RideHandle getRideHandle();
    void setRideHandle(RideHandle rideHandle);

    TriggerContext getTriggerContext();

    void changeMode(ControlMode newControlMode);

    Ride getRide();

    void onTrainArrive(Train train, StationHandle stationHandle);

    void onTrainDepart(Train train, StationHandle stationHandle);

    ControlMode getControlMode();
    void setControlMode(ControlMode controlMode);

    boolean setOperator(Player player);

    Player getOperator();

    boolean isActive();
    void setActive(boolean active);

}
