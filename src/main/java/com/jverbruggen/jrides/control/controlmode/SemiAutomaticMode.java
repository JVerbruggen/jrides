package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class SemiAutomaticMode extends BaseControlMode implements ControlMode {

    public SemiAutomaticMode(StationHandle stationHandle, DispatchLockCollection dispatchLockCollection) {
        super(stationHandle, dispatchLockCollection);
    }

    @Override
    public void tick() {
        super.tick();

        MinMaxWaitingTimer waitingTimer = getWaitingTimer();

        Train stationaryTrain = stationHandle.getStationaryTrain();
        if(stationaryTrain != null){
            waitingTimer.sendGenericWaitingNotification(stationaryTrain.getPassengers());
        }
    }

    @Override
    public void onPlayerEnter(Seat seat, Player player) {

    }

    @Override
    public void onPlayerExit(Seat seat, Player player) {

    }

    @Override
    public void stopOperating() {

    }

    @Override
    public void onDispatch() {

    }

    @Override
    public boolean allowsAction(ControlAction action) {
        return true;
    }
}
