package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.DebounceCall;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import org.bukkit.Bukkit;

public class AutomaticMode implements ControlMode{
    private TriggerContext triggerContext;
    private final StationHandle stationHandle;

    private final long tickInterval;
    private final MinMaxWaitingTimer waitingTimer;
    private boolean dispatchIntervalActive;
    private boolean started;

    private DispatchLockCollection dispatchLockCollection;
    private DebounceCall dispatchDebounce;

    public AutomaticMode(StationHandle stationHandle, DispatchLockCollection dispatchLockCollection, MinMaxWaitingTimer waitingTimer) {
        this.stationHandle = stationHandle;
        this.dispatchDebounce = new DebounceCall(20);
        this.dispatchLockCollection = dispatchLockCollection;
        this.started = false;
        this.tickInterval = 5L;

        this.waitingTimer = waitingTimer;
        this.dispatchIntervalActive = false;
    }

    public void tick() {
        stationTick();
    }

    private void stationTick(){
        if(dispatchIntervalActive) waitingTimer.increment(tickInterval);

        Train stationaryTrain = stationHandle.getStationaryTrain();
        if(stationaryTrain != null)
            waitingTimer.sendTimeWaitingNotification(stationaryTrain.getPassengers());

        if(!dispatchIntervalReached()) return;
        if(!dispatchLockCollection.allUnlocked()) return;

        dispatchDebounce.run(() -> triggerContext.getDispatchTrigger().dispatch());
    }

    private boolean dispatchIntervalReached(){
        return waitingTimer.reachedPreferred();
    }

    @Override
    public void setTriggerContext(TriggerContext triggerContext) {
        this.triggerContext = triggerContext;
    }

    @Override
    public void onTrainArrive(Train train) {
        dispatchIntervalActive = true;
        waitingTimer.reset();
        dispatchDebounce.reset();
        stationHandle.openEntryGates();
    }

    @Override
    public void onTrainDepart(Train train) {
        dispatchIntervalActive = false;
        waitingTimer.reset();
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
    public void startOperating() {
        if(started) throw new RuntimeException("Mode already started");

        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, this.tickInterval, this.tickInterval);
        started = true;
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

    @Override
    public MinMaxWaitingTimer getWaitingTimer() {
        return waitingTimer;
    }
}
