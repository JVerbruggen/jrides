package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;
import com.jverbruggen.jrides.models.ride.gate.Gate;

import java.util.List;

public class StationHandle {
    private String name;
    private String shortName;
    private final List<Gate> entryGates;
    private final PlayerLocation ejectLocation;
    private final MinMaxWaitingTimer waitingTimer;
    private TriggerContext triggerContext;
    private final List<TrainEffectTriggerHandle> entryBlockingEffectTriggers;
    private final List<TrainEffectTriggerHandle> exitBlockingEffectTriggers;
    private final List<TrainEffectTriggerHandle> exitEffectTriggers;

    public StationHandle(String name, String shortName, List<Gate> entryGates, PlayerLocation ejectLocation, MinMaxWaitingTimer waitingTimer, TriggerContext triggerContext, List<TrainEffectTriggerHandle> entryBlockingEffectTriggers, List<TrainEffectTriggerHandle> exitBlockingEffectTriggers, List<TrainEffectTriggerHandle> exitEffectTriggers) {
        this.name = name;
        this.shortName = shortName;
        this.entryGates = entryGates;
        this.ejectLocation = ejectLocation;
        this.waitingTimer = waitingTimer;
        this.triggerContext = triggerContext;
        this.entryBlockingEffectTriggers = entryBlockingEffectTriggers;
        this.exitBlockingEffectTriggers = exitBlockingEffectTriggers;
        this.exitEffectTriggers = exitEffectTriggers;

        triggerContext.getRestraintTrigger().setStationHandle(this);
        triggerContext.getGateTrigger().setStationHandle(this);
    }

    public PlayerLocation getEjectLocation() {
        return ejectLocation;
    }

    public List<Gate> getEntryGates() {
        return entryGates;
    }

    public TriggerContext getTriggerContext() {
        return triggerContext;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
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

    public MinMaxWaitingTimer getWaitingTimer() {
        return waitingTimer;
    }

    public void closeRestraints() {
        triggerContext.getRestraintTrigger().getLock().setLocked(false);
    }

    public boolean hasVehicle(){
        throw new RuntimeException("Has vehicle is not implemented yet for non-coaster station handle");
    }

    public Vehicle getStationaryVehicle(){
        throw new RuntimeException("Get vehicle is not implemented yet for non-coaster station handle");
    }

    @Override
    public String toString() {
        return "StationHandle{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }

    public void runEntryEffectTriggers(Train train){
        if(entryBlockingEffectTriggers == null) return;
        entryBlockingEffectTriggers.forEach(t -> t.executeForTrain(train));
    }

    public void runExitEffectTriggers(Train train){
        if(exitBlockingEffectTriggers != null)
            exitBlockingEffectTriggers.forEach(t -> t.executeForTrain(train));
        if(exitEffectTriggers != null)
            exitEffectTriggers.forEach(t -> t.executeForTrain(train));
    }

    public boolean entryEffectTriggersDone(){
        if(entryBlockingEffectTriggers == null) return true;
        return entryBlockingEffectTriggers.stream().allMatch(t -> t.getTrainEffectTrigger().finishedPlaying());
    }

    public boolean exitEffectTriggersDone(){
        if(exitBlockingEffectTriggers == null) return true;
        return exitBlockingEffectTriggers.stream().allMatch(t -> t.getTrainEffectTrigger().finishedPlaying());
    }
}
