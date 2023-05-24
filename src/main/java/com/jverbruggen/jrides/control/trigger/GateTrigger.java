package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileFields;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class GateTrigger implements StationTrigger{
    private CoasterStationHandle stationHandle;
    private final DispatchLock gatesLock;
    private final LanguageFile languageFile;

    public GateTrigger(DispatchLock gatesLock) {
        this.stationHandle = null;
        this.gatesLock = gatesLock;
        this.languageFile = JRidesPlugin.getLanguageFile();
    }

    @Override
    public void setStationHandle(CoasterStationHandle stationHandle) {
        this.stationHandle = stationHandle;
    }

    @Override
    public DispatchLock getLock() {
        return gatesLock;
    }

    @Override
    public boolean execute(MessageReceiver messageReceiver) {
        if(stationHandle == null){
            JRidesPlugin.getLogger().severe("No station handle set for gate trigger");
            return false;
        }

        Train stationaryTrain = stationHandle.getStationaryTrain();
        if(stationaryTrain == null) {
            languageFile.sendMessage(messageReceiver, LanguageFileFields.NOTIFICATION_RIDE_NO_TRAIN_PRESENT);
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
