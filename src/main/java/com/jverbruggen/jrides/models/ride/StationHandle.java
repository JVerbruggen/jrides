package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
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

    public StationHandle(String name, String shortName, List<Gate> entryGates, PlayerLocation ejectLocation, MinMaxWaitingTimer waitingTimer, TriggerContext triggerContext) {
        this.name = name;
        this.shortName = shortName;
        this.entryGates = entryGates;
        this.ejectLocation = ejectLocation;
        this.waitingTimer = waitingTimer;
        this.triggerContext = triggerContext;
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
}
