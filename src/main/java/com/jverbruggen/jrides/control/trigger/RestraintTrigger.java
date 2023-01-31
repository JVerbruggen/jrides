package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class RestraintTrigger implements StationTrigger {
    private StationHandle stationHandle;
    private final DispatchLock restraintLock;
    private final LanguageFile languageFile;

    public RestraintTrigger(DispatchLock restraintLock) {
        this.stationHandle = null;
        this.restraintLock = restraintLock;
        this.languageFile = JRidesPlugin.getLanguageFile();
    }

    @Override
    public void setStationHandle(StationHandle stationHandle) {
        this.stationHandle = stationHandle;
    }

    @Override
    public DispatchLock getLock() {
        return restraintLock;
    }

    @Override
    public boolean execute(MessageReceiver messageReceiver) {
        if(stationHandle == null){
            JRidesPlugin.getLogger().severe("No station handle set for restraint trigger");
            return false;
        }

        Train stationaryTrain = stationHandle.getStationaryTrain();
        if(stationaryTrain == null) {
            languageFile.sendMessage(messageReceiver, languageFile.notificationRideNoTrainPresent);
            return false;
        }

        // Toggles between on and off
        boolean newLockedState = !stationaryTrain.getRestraintState();
        stationaryTrain.setRestraintForAll(newLockedState);
        restraintLock.setLocked(!newLockedState);

        return true;
    }
}
