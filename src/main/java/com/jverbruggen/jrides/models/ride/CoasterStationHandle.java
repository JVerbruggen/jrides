package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;
import com.jverbruggen.jrides.models.ride.gate.Gate;

import java.util.List;

public class CoasterStationHandle extends StationHandle {
    private Train stationaryTrain;
    private CoasterHandle coasterHandle;
    private final List<TrainEffectTriggerHandle> entryEffectTriggers;
    private final List<TrainEffectTriggerHandle> exitEffectTriggers;

    public CoasterStationHandle(CoasterHandle coasterHandle, String name, String shortName, TriggerContext triggerContext, List<Gate> entryGates, MinMaxWaitingTimer waitingTimer, List<TrainEffectTriggerHandle> entryEffectTriggers, List<TrainEffectTriggerHandle> exitEffectTriggers, PlayerLocation ejectLocation){
        super(name, shortName, entryGates, ejectLocation, waitingTimer, triggerContext);
        this.coasterHandle = coasterHandle;
        this.entryEffectTriggers = entryEffectTriggers;
        this.exitEffectTriggers = exitEffectTriggers;
        this.stationaryTrain = null;

        triggerContext.getRestraintTrigger().setStationHandle(this);
        triggerContext.getGateTrigger().setStationHandle(this);
        coasterHandle.addStationHandle(this);
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
        if(entryEffectTriggers == null) return;
        entryEffectTriggers.forEach(t -> t.execute(train));
    }

    public void runExitEffectTriggers(Train train){
        if(exitEffectTriggers == null) return;
        exitEffectTriggers.forEach(t -> t.execute(train));
    }

    public boolean entryEffectTriggersDone(){
        if(entryEffectTriggers == null) return true;
        return entryEffectTriggers.stream().allMatch(t -> t.getTrainEffectTrigger().finishedPlaying());
    }

    public boolean exitEffectTriggersDone(){
        if(exitEffectTriggers == null) return true;
        return exitEffectTriggers.stream().allMatch(t -> t.getTrainEffectTrigger().finishedPlaying());
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

    @Override
    public void closeRestraints(){
        if(stationaryTrain == null) return;

        if(stationaryTrain.getRestraintState()) return;

        stationaryTrain.setRestraintForAll(true);
        getTriggerContext().getRestraintTrigger().getLock().setLocked(false);
    }

    public void onPlayerEnter(Player player){
        int passengerCount = getStationaryTrain().getPassengers().size();
        if(passengerCount == 1){
            getCoasterHandle().getRideController().getControlMode().getWaitingTimer().setPreferredWaitingTimeFromNow(15);
        }
    }

    public boolean shouldEject(){
        return true;
    }

    public boolean isExit(){
        return true;
    }

    @Override
    public boolean hasVehicle() {
        return hasTrain();
    }

    @Override
    public Vehicle getStationaryVehicle() {
        return getStationaryTrain();
    }
}
