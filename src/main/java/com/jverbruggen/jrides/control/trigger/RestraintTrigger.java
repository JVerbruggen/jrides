package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileFields;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class RestraintTrigger implements StationTrigger {
    private CoasterStationHandle stationHandle;
    private final DispatchLock restraintLock;
    private final LanguageFile languageFile;

    public RestraintTrigger(DispatchLock restraintLock) {
        this.stationHandle = null;
        this.restraintLock = restraintLock;
        this.languageFile = JRidesPlugin.getLanguageFile();
    }

    @Override
    public void setStationHandle(CoasterStationHandle stationHandle) {
        this.stationHandle = stationHandle;
    }

    @Override
    public DispatchLock getLock() {
        return restraintLock;
    }

    @Override
    public boolean execute(MessageAgent messageAgent) {
        if(messageAgent != null && !messageAgent.hasPermission(Permissions.CABIN_OPERATE)){
            languageFile.sendMessage(messageAgent, LanguageFileFields.ERROR_OPERATING_NO_PERMISSION);
            return false;
        }

        if(stationHandle == null){
            JRidesPlugin.getLogger().severe("No station handle set for restraint trigger");
            return false;
        }

        Train stationaryTrain = stationHandle.getStationaryTrain();
        if(stationaryTrain == null) {
            languageFile.sendMessage(messageAgent, LanguageFileFields.NOTIFICATION_RIDE_NO_TRAIN_PRESENT);
            return false;
        }

        // Toggles between on and off
        boolean newLockedState = !stationaryTrain.getRestraintState();
        stationaryTrain.setRestraintForAll(newLockedState);
        restraintLock.setLocked(!newLockedState);

        return true;
    }
}
