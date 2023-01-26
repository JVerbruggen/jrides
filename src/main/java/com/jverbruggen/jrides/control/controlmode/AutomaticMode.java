package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.trackbehaviour.StationPhase;
import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.DebounceCall;
import com.jverbruggen.jrides.models.ride.Seat;
import org.bukkit.Bukkit;

public class AutomaticMode implements ControlMode{
    private TriggerContext triggerContext;
    private int minimumDispatchInterval;
    private int maximumDispatchInterval;
    private int dispatchIntervalState;

    private DispatchLockCollection dispatchLockCollection;
    private DebounceCall dispatchDebounce;
    private StationPhase stationPhase;

    public AutomaticMode() {
        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 5L, 5L);
    }

    public void tick() {
        stationTick();
    }

    private void stationTick(){
        if(!dispatchIntervalReached()) return;
        if(!dispatchLockCollection.allUnlocked()) return;

        JRidesPlugin.getLogger().info("Dispatch");

        dispatchDebounce.run(() -> triggerContext.getDispatchTrigger().dispatch());
    }

    private boolean dispatchIntervalReached(){
        return minimumDispatchInterval <= dispatchIntervalState;
    }

    @Override
    public void setTriggerContext(TriggerContext triggerContext) {
        this.triggerContext = triggerContext;
    }

    @Override
    public void onPlayerEnter(Seat seat, Player player) {

    }

    @Override
    public void onPlayerExit(Seat seat, Player player) {

    }

    @Override
    public void startOperating() {

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
