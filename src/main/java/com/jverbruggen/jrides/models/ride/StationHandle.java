package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.gate.Gate;

import java.util.List;

public class StationHandle {
    private TriggerContext triggerContext;
    private Train stationaryTrain;
    private String name;
    private final List<Gate> entryGates;
    private CoasterHandle coasterHandle;
    private final MinMaxWaitingTimer waitingTimer;
    private final List<EffectTriggerHandle> entryEffectTriggers;
    private final List<EffectTriggerHandle> exitEffectTriggers;
    private final PlayerLocation ejectLocation;

    public StationHandle(CoasterHandle coasterHandle, String name, TriggerContext triggerContext, List<Gate> entryGates, MinMaxWaitingTimer waitingTimer, List<EffectTriggerHandle> entryEffectTriggers, List<EffectTriggerHandle> exitEffectTriggers, PlayerLocation ejectLocation){
        this.coasterHandle = coasterHandle;
        this.triggerContext = triggerContext;
        this.entryGates = entryGates;
        this.waitingTimer = waitingTimer;
        this.entryEffectTriggers = entryEffectTriggers;
        this.exitEffectTriggers = exitEffectTriggers;
        this.stationaryTrain = null;
        this.name = name;
        this.ejectLocation = ejectLocation;

        triggerContext.getRestraintTrigger().setStationHandle(this);
        triggerContext.getGateTrigger().setStationHandle(this);
        coasterHandle.addStationHandle(this);
    }

    public PlayerLocation getEjectLocation() {
        return ejectLocation;
    }

    public List<Gate> getEntryGates() {
        return entryGates;
    }

    public String getName() {
        return name;
    }

    public TriggerContext getTriggerContext() {
        return triggerContext;
    }

    public boolean hasTrain(){
        return stationaryTrain != null;
    }

    public Train getStationaryTrain(){
        return stationaryTrain;
    }

    public CoasterHandle getCoasterHandle() {
        return coasterHandle;
    }

    public void runEntryEffectTriggers(Train train){
        entryEffectTriggers.forEach(t -> t.execute(train));
    }

    public void runExitEffectTriggers(Train train){
        exitEffectTriggers.forEach(t -> t.execute(train));
    }

    public boolean entryEffectTriggersDone(){
        return entryEffectTriggers.stream().allMatch(t -> t.getEffectTrigger().finishedPlaying());
    }

    public boolean exitEffectTriggersDone(){
        return exitEffectTriggers.stream().allMatch(t -> t.getEffectTrigger().finishedPlaying());
    }

    public void setStationaryTrain(Train train) {
        if(train == null){
            if(this.stationaryTrain != null)
                this.stationaryTrain.setStationaryAt(null);
            this.stationaryTrain = null;
            return;
        }

        if(stationaryTrain != null) throw new RuntimeException("Two trains cannot be in the same station!");
        this.stationaryTrain = train;
        train.setStationaryAt(this);
    }

    public void openEntryGates(){
        entryGates.forEach(Gate::open);
    }

    public void closeEntryGates(){
        entryGates.forEach(Gate::close);
        // TODO: teleport everyone awae
    }

    public boolean areEntryGatesClosed(){
        return entryGates.stream().noneMatch(Gate::isOpen);
    }

    public void onPlayerEnter(Player player){
        int passengerCount = getStationaryTrain().getPassengers().size();
        if(passengerCount == 1){
            getCoasterHandle().getRideController().getControlMode().getWaitingTimer().setPreferredWaitingTimeFromNow(15);
        }
    }

    public MinMaxWaitingTimer getWaitingTimer() {
        return waitingTimer;
    }

    public boolean shouldEject(){
        return true;
    }

    public boolean isExit(){
        return true;
    }
}
