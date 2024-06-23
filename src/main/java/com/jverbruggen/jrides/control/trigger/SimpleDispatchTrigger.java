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

import com.jverbruggen.jrides.common.Result;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

import java.util.List;

public class SimpleDispatchTrigger implements DispatchTrigger, StationTrigger {
    private StationHandle stationHandle;
    private boolean active;
    private final DispatchLockCollection dispatchLockCollection;
    private final LanguageFile languageFile;

    public SimpleDispatchTrigger(DispatchLockCollection dispatchLockCollection) {
        this.stationHandle = null;
        this.active = false;
        this.dispatchLockCollection = dispatchLockCollection;
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    @Override
    public boolean execute(MessageAgent messageAgent){
        Result canExecuteResult = canExecute(messageAgent);
        if(!canExecuteResult.ok()){
            canExecuteResult.sendMessageTo(messageAgent);
            return false;
        }

        active = true;
        return true;
    }

    @Override
    public Result canExecute(MessageAgent messageAgent) {
        if(messageAgent != null && !messageAgent.hasPermission(Permissions.CABIN_OPERATE)){
            return Result.isNotOk(languageFile.get(LanguageFileField.ERROR_OPERATING_NO_PERMISSION));
        }
        if(!stationHandle.canOperate(messageAgent)){
            return Result.isNotOk(languageFile.get(LanguageFileField.ERROR_OPERATING_NO_PERMISSION));
        }
        if(!dispatchLockCollection.allUnlocked()){
            if(messageAgent != null){
                List<String> problemList = dispatchLockCollection.getProblems(Integer.MAX_VALUE, messageAgent.hasPermission(Permissions.ELEVATED_DISPATCH_PROBLEMS_DEBUG));
                problemList.add(0, languageFile.get(LanguageFileField.NOTIFICATION_RIDE_DISPATCH_PROBLEMS));
                return Result.isNotOk(problemList);
            }else{
                return Result.isNotOk();
            }
        }

        return Result.isOk();
    }

    @Override
    public void reset(){
        active = false;
    }

    @Override
    public boolean isActive(){
        return active;
    }

    @Override
    public DispatchLockCollection getDispatchLockCollection() {
        return dispatchLockCollection;
    }

    @Override
    public void setStationHandle(StationHandle stationHandle) {
        this.stationHandle = stationHandle;
    }

    @Override
    public DispatchLock getLock() {
        return dispatchLockCollection;
    }
}
