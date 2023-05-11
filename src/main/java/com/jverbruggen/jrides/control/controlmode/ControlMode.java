package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public interface ControlMode {
    void tick();
    void setTriggerContext(TriggerContext triggerContext);
    TriggerContext getTriggerContext();

    void onVehicleArrive(Train train, StationHandle stationHandle);
    void onVehicleDepart(Train train, StationHandle stationHandle);
//    void onPlayerEnter(Seat seat, Player player);
//    void onPlayerExit(Seat seat, Player player);
//    void onDispatch();
    boolean setOperator(Player player);
    boolean allowsAction(ControlAction action, Player player);

    MinMaxWaitingTimer getWaitingTimer();
    Player getOperator();
    StationHandle getStationHandle();
}
