package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;

public interface ControlMode {
    void tick();

    void onVehicleArrive(Vehicle vehicle, StationHandle stationHandle);
    void onVehicleDepart(Vehicle vehicle, StationHandle stationHandle);
    boolean setOperator(Player player);
    boolean allowsAction(ControlAction action, Player player);

    MinMaxWaitingTimer getWaitingTimer();
    Player getOperator();
    void setTriggerContext(TriggerContext triggerContext);
}
