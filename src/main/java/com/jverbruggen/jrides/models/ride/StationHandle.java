package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import com.jverbruggen.jrides.models.ride.gate.Gate;
import org.bukkit.Bukkit;

import java.util.List;

public class StationHandle {
    private TriggerContext triggerContext;
    private Train stationaryTrain;
    private String name;
    private final List<Gate> entryGates;
    private CoasterHandle coasterHandle;

    public StationHandle(CoasterHandle coasterHandle, String name, TriggerContext triggerContext, List<Gate> entryGates){
        this.coasterHandle = coasterHandle;
        this.triggerContext = triggerContext;
        this.entryGates = entryGates;
        this.stationaryTrain = null;
        this.name = name;

        coasterHandle.addStationHandle(this);
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

    public void onPlayerEnter(Player player){
        int passengerCount = getStationaryTrain().getPassengers().size();
        if(passengerCount == 1){
            getCoasterHandle().getRideController().getControlMode().getWaitingTimer().setPreferredWaitingTimeFromNow(15);
        }
    }

}
