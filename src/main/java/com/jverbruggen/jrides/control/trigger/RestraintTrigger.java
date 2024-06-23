/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.common.Result;
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
        Result canExecuteResult = canExecute(messageAgent);
        if(!canExecuteResult.ok()){
            canExecuteResult.sendMessageTo(messageAgent);
            return false;
        }

        Vehicle stationaryTrain = stationHandle.getStationaryVehicle();
        if(!setRestraintsState(!stationaryTrain.getRestraintState())) {
            languageFile.sendMessage(messageAgent, LanguageFileField.NOTIFICATION_RIDE_NO_TRAIN_PRESENT);
            return false;
        }

        return true;
    }

    @Override
    public Result canExecute(MessageAgent messageAgent) {
        if(messageAgent != null && !messageAgent.hasPermission(Permissions.CABIN_OPERATE)){
            return Result.isNotOk(languageFile.get(LanguageFileField.ERROR_OPERATING_NO_PERMISSION));
        }

        if(stationHandle == null){
            JRidesPlugin.getLogger().severe("No station handle set for restraint trigger");
            return Result.isNotOk();
        }

        Vehicle stationaryTrain = stationHandle.getStationaryVehicle();

        if(stationaryTrain == null) {
            return Result.isNotOk(languageFile.get(LanguageFileField.NOTIFICATION_RIDE_NO_TRAIN_PRESENT));
        }

        return Result.isOk();
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
