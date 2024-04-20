package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;

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
    public boolean execute(MessageAgent messageAgent) {
        if(!canExecute(messageAgent))
            return false;

        Vehicle stationaryTrain = stationHandle.getStationaryVehicle();
        if(!setRestraintsState(!stationaryTrain.getRestraintState())) {
            languageFile.sendMessage(messageAgent, LanguageFileField.NOTIFICATION_RIDE_NO_TRAIN_PRESENT);
            return false;
        }

        return true;
    }

    @Override
    public boolean canExecute(MessageAgent messageAgent) {
        if(messageAgent != null && !messageAgent.hasPermission(Permissions.CABIN_OPERATE)){
            languageFile.sendMessage(messageAgent, LanguageFileField.ERROR_OPERATING_NO_PERMISSION);
            return false;
        }

        if(stationHandle == null){
            JRidesPlugin.getLogger().severe("No station handle set for restraint trigger");
            return false;
        }

        Vehicle stationaryTrain = stationHandle.getStationaryVehicle();

        if(stationaryTrain == null) {
            languageFile.sendMessage(messageAgent, LanguageFileField.NOTIFICATION_RIDE_NO_TRAIN_PRESENT);
            return false;
        }

        return true;
    }

    public boolean setRestraintsState(boolean closed){
        if(restraintLock.isUnlocked() == closed) return false;

        Vehicle stationaryTrain = stationHandle.getStationaryVehicle();
        if(stationaryTrain == null) {
            return false;
        }

        stationaryTrain.setRestraintForAll(closed);
        restraintLock.setLocked(!closed);
        return true;
    }
}
