package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.control.ControlAction;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public interface ControlMode {
    void setTriggerContext(TriggerContext triggerContext);

    void onTrainArrive(Train train);
    void onTrainDepart(Train train);
    void onPlayerEnter(Seat seat, Player player);
    void onPlayerExit(Seat seat, Player player);
    void startOperating();
    void stopOperating();
    void onDispatch();
    boolean allowsAction(ControlAction action);
}
