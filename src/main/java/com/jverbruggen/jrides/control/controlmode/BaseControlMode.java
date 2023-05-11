package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public abstract class BaseControlMode implements ControlMode {
    protected final StationHandle stationHandle;
    protected TriggerContext triggerContext;
    protected DispatchLockCollection dispatchLockCollection;
    protected MinMaxWaitingTimer waitingTimer;

    private final long tickInterval;
    private boolean dispatchIntervalActive;
    private boolean started;

    private int tickIntervalState;


    protected BaseControlMode(StationHandle stationHandle, MinMaxWaitingTimer waitingTimer, DispatchLockCollection dispatchLockCollection) {
        this.stationHandle = stationHandle;
        this.triggerContext = null;
        this.dispatchLockCollection = dispatchLockCollection;
        this.waitingTimer = waitingTimer;

        this.dispatchIntervalActive = stationHandle.hasVehicle();
        this.tickInterval = 5L;
        this.tickIntervalState = 0;
    }

    @Override
    public void tick() {
        if(tickIntervalState < tickInterval-1){
            tickIntervalState++;
            return;
        }
        tickIntervalState = 0;

        if(dispatchIntervalActive) waitingTimer.increment(tickInterval);
    }

    @Override
    public void setTriggerContext(TriggerContext triggerContext) {
        this.triggerContext = triggerContext;
    }

    @Override
    public MinMaxWaitingTimer getWaitingTimer() {
        return waitingTimer;
    }

    @Override
    public void onVehicleArrive(Train train, StationHandle stationHandle) {
        waitingTimer.reset();
        dispatchIntervalActive = true;
    }

    @Override
    public void onVehicleDepart(Train train, StationHandle stationHandle) {
        waitingTimer.reset();
        dispatchIntervalActive = false;
    }
}
