package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.DebounceCall;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.Bukkit;

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
        if(stationaryTrain == null) return;

        int visualTime = waitingTimer.getVisualDispatchTime(waitingTimer.timeUntilPreferredWaitingTime());
        waitingTimer.sendTimeWaitingNotification(stationaryTrain.getPassengers(), visualTime);

        if(!waitingTimer.reachedFunction()) return;
        stationHandle.closeEntryGates();
        stationHandle.closeRestraints();

        if(!dispatchLockCollection.allUnlocked()) return;

        dispatchDebounce.run(() -> triggerContext.getDispatchTrigger().execute(null));
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
    public void onDispatch() {
        dispatchDebounce.reset();
    }

    @Override
    public boolean setOperator(Player player) {
        return false;
    }

    @Override
    public boolean allowsAction(ControlAction action, Player player) {
        return false;
    }

    @Override
    public Player getOperator() {
        return null;
    }
}
