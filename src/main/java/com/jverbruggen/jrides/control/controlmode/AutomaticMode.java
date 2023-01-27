package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.DebounceCall;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class AutomaticMode extends BaseControlMode implements ControlMode{
    private DebounceCall dispatchDebounce;

    public AutomaticMode(StationHandle stationHandle, DispatchLockCollection dispatchLockCollection) {
        super(stationHandle, dispatchLockCollection);

        this.dispatchDebounce = new DebounceCall(20);
    }

    @Override
    public void tick() {
        super.tick();

        stationTick();
    }

    private void stationTick(){
        MinMaxWaitingTimer waitingTimer = getWaitingTimer();

        Train stationaryTrain = stationHandle.getStationaryTrain();
        if(stationaryTrain != null){
            int visualTime = waitingTimer.getVisualDispatchTime(waitingTimer.timeUntilPreferredWaitingTime());
            waitingTimer.sendTimeWaitingNotification(stationaryTrain.getPassengers(), visualTime);
        }

        if(!waitingTimer.reachedPreferred()) return;
        if(!dispatchLockCollection.allUnlocked()) return;

        dispatchDebounce.run(() -> triggerContext.getDispatchTrigger().dispatch());
    }

    @Override
    public void onTrainArrive(Train train) {
        super.onTrainArrive(train);

        dispatchDebounce.reset();
        stationHandle.openEntryGates();
    }

    @Override
    public void onTrainDepart(Train train) {
        super.onTrainDepart(train);

        dispatchDebounce.reset();
        stationHandle.closeEntryGates();
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
        dispatchDebounce.reset();
    }

    @Override
    public boolean allowsAction(ControlAction action) {
        return false;
    }
}
