package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class RestraintTrigger implements StationTrigger {
    private StationHandle stationHandle;
    private final DispatchLock restraintLock;

    public RestraintTrigger(DispatchLock restraintLock) {
        this.stationHandle = null;
        this.restraintLock = restraintLock;
    }

    @Override
    public void setStationHandle(StationHandle stationHandle) {
        this.stationHandle = stationHandle;
    }

    @Override
    public boolean execute(MessageReceiver messageReceiver) {
        if(stationHandle == null) throw new RuntimeException("No station handle set for restraint trigger");

        Train stationaryTrain = stationHandle.getStationaryTrain();
        if(stationaryTrain == null) {
            messageReceiver.sendMessage("No train in station");
            return false;
        }

        // Toggles between on and off
        boolean newLockedState = !stationaryTrain.getRestraintState();
        stationaryTrain.setRestraintForAll(newLockedState);
        restraintLock.setLocked(!newLockedState);

        return true;
    }
}
