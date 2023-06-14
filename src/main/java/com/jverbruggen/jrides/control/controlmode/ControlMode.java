package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.trigger.Trigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public interface ControlMode {
    void tick();

    void onVehicleArrive(Train train, StationHandle stationHandle);
    void onVehicleDepart(Train train, StationHandle stationHandle);
    boolean setOperator(Player player);
    boolean allowsAction(ControlAction action, Player player);

    MinMaxWaitingTimer getWaitingTimer();
    Player getOperator();
    void setTriggerContext(TriggerContext triggerContext);
}
