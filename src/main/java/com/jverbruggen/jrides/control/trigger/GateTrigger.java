package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class GateTrigger implements StationTrigger{
    private StationHandle stationHandle;
    private final DispatchLock gatesLock;

    public GateTrigger(DispatchLock gatesLock) {
        this.stationHandle = null;
        this.gatesLock = gatesLock;
    }

    @Override
    public void setStationHandle(StationHandle stationHandle) {
        this.stationHandle = stationHandle;
    }

    @Override
    public DispatchLock getLock() {
        return gatesLock;
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
        boolean allClosed = stationHandle.areEntryGatesClosed();
        if(allClosed){
            gatesLock.setLocked(true);
            stationHandle.openEntryGates();
        }else{
            stationHandle.closeEntryGates();
            gatesLock.setLocked(false);
        }

        return true;
    }
}
