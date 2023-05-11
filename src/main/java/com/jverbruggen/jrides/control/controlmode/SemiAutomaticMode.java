package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;

public class SemiAutomaticMode extends BaseControlMode implements ControlMode {

    public SemiAutomaticMode(StationHandle stationHandle, MinMaxWaitingTimer waitingTimer, DispatchLockCollection dispatchLockCollection) {
        super(stationHandle, waitingTimer, dispatchLockCollection, stationHandle.hasVehicle());

        waitingTimer.setReachedTimeFunction(waitingTimer::reachedMinimum);
    }

    @Override
    public void tick() {
        super.tick();

        MinMaxWaitingTimer waitingTimer = getWaitingTimer();

        Vehicle stationaryVehicle = stationHandle.getStationaryVehicle();
        if(stationaryVehicle != null){
            waitingTimer.sendGenericWaitingNotification(stationaryVehicle.getPassengers());
        }
    }

}
