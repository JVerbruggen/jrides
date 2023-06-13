package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.DebounceCall;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;

public class AutomaticMode extends BaseControlMode implements ControlMode{
    private DebounceCall dispatchDebounce;

    public AutomaticMode(RideHandle rideHandle, StationHandle stationHandle, MinMaxWaitingTimer waitingTimer, DispatchLockCollection dispatchLockCollection) {
        super(rideHandle, stationHandle, waitingTimer, dispatchLockCollection, stationHandle.hasVehicle());

        this.dispatchDebounce = new DebounceCall(20);
    }

    @Override
    public void tick() {
        super.tick();

        stationTick();
    }

    private void stationTick(){
        if(!rideHandle.isOpen()) return;

        MinMaxWaitingTimer waitingTimer = getWaitingTimer();

        Vehicle stationaryVehicle = stationHandle.getStationaryVehicle();
        if(stationaryVehicle == null) return;

        int visualTime = waitingTimer.getVisualDispatchTime(waitingTimer.timeUntilPreferredWaitingTime());
        waitingTimer.sendTimeWaitingNotification(stationaryVehicle.getPassengers(), visualTime);

        if(!waitingTimer.reachedFunction()) return;
        stationHandle.closeEntryGates();
        stationHandle.closeRestraints();

        if(dispatchLockCollection != null && !dispatchLockCollection.allUnlocked()) return;

        dispatchDebounce.run(() -> triggerContext.getDispatchTrigger().execute(null));
    }

    @Override
    public void onVehicleArrive(Train train, StationHandle stationHandle) {
        super.onVehicleArrive(train, stationHandle);

        dispatchDebounce.reset();
        stationHandle.openEntryGates();
    }

    @Override
    public void onVehicleDepart(Train train, StationHandle stationHandle) {
        super.onVehicleDepart(train, stationHandle);

        dispatchDebounce.reset();
        stationHandle.closeEntryGates();
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
