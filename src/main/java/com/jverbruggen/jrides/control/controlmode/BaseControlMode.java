package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import org.bukkit.Bukkit;

public abstract class BaseControlMode implements ControlMode {
    protected final StationHandle stationHandle;
    protected TriggerContext triggerContext;
    protected DispatchLockCollection dispatchLockCollection;

    private final long tickInterval;
    private boolean dispatchIntervalActive;
    private boolean started;


    protected BaseControlMode(StationHandle stationHandle, DispatchLockCollection dispatchLockCollection) {
        this.stationHandle = stationHandle;
        this.triggerContext = null;
        this.dispatchLockCollection = dispatchLockCollection;

        this.dispatchIntervalActive = false;
        this.tickInterval = 5L;
    }

    @Override
    public void tick() {
        MinMaxWaitingTimer waitingTimer = getWaitingTimer();
        if(dispatchIntervalActive) waitingTimer.increment(tickInterval);
    }

    @Override
    public void startOperating() {
        if(started) throw new RuntimeException("Mode already started");

        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, this.tickInterval, this.tickInterval);
        started = true;
    }

    @Override
    public void setTriggerContext(TriggerContext triggerContext) {
        this.triggerContext = triggerContext;
    }

    @Override
    public MinMaxWaitingTimer getWaitingTimer() {
        return stationHandle.getWaitingTimer();
    }

    @Override
    public void onTrainArrive(Train train) {
        getWaitingTimer().reset();
        dispatchIntervalActive = true;
    }

    @Override
    public void onTrainDepart(Train train) {
        getWaitingTimer().reset();
        dispatchIntervalActive = false;
    }
}
