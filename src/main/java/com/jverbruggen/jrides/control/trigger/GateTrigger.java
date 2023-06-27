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

public class GateTrigger implements StationTrigger{
    private StationHandle stationHandle;
    private final DispatchLock gatesLock;
    private final LanguageFile languageFile;

    public GateTrigger(DispatchLock gatesLock) {
        this.stationHandle = null;
        this.gatesLock = gatesLock;
        this.languageFile = JRidesPlugin.getLanguageFile();
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
    public boolean execute(MessageAgent messageAgent) {
        if(messageAgent != null && !messageAgent.hasPermission(Permissions.CABIN_OPERATE)){
            languageFile.sendMessage(messageAgent, LanguageFileField.ERROR_OPERATING_NO_PERMISSION);
            return false;
        }

        if(stationHandle == null){
            JRidesPlugin.getLogger().severe("No station handle set for gate trigger");
            return false;
        }

        boolean set = setGatesState(!stationHandle.areEntryGatesClosed());
        if(!set){
            languageFile.sendMessage(messageAgent, LanguageFileField.NOTIFICATION_RIDE_NO_TRAIN_PRESENT);
            return false;
        }

        return true;
    }

    public boolean setGatesState(boolean closed){
        if(gatesLock.isUnlocked() == closed) return false;

        Vehicle stationaryTrain = stationHandle.getStationaryVehicle();
        if(stationaryTrain == null) {
            return false;
        }

        if(closed){
            stationHandle.closeEntryGates();
            gatesLock.setLocked(false);
        }else{
            gatesLock.setLocked(true);
            stationHandle.openEntryGates();
        }

        return true;
    }
}
