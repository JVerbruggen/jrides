package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;

public class VoidMode implements ControlMode {

    @Override
    public void tick() {

    }

    @Override
    public void onVehicleArrive(Vehicle vehicle, StationHandle stationHandle) {

    }

    @Override
    public void onVehicleDepart(Vehicle vehicle, StationHandle stationHandle) {

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
    public MinMaxWaitingTimer getWaitingTimer() {
        return null;
    }

    @Override
    public Player getOperator() {
        return null;
    }

    @Override
    public void setTriggerContext(TriggerContext triggerContext) {

    }
}
