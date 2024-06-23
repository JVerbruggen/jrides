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
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

import java.util.List;

public class MultiDispatchTrigger implements DispatchTrigger {
    private final List<DispatchTrigger> dispatchTriggerList;
    private final DispatchLockCollection dispatchLockCollection;
    private final LanguageFile languageFile;

    public MultiDispatchTrigger(List<DispatchTrigger> dispatchTriggerList, DispatchLockCollection dispatchLockCollection) {
        this.dispatchTriggerList = dispatchTriggerList;
        this.dispatchLockCollection = dispatchLockCollection;
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    @Override
    public boolean execute(MessageAgent messageAgent){
        Result canExecuteResult = canExecute(messageAgent);
        if(!canExecuteResult.ok()){
            languageFile.sendMessage(messageAgent, languageFile.get(LanguageFileField.NOTIFICATION_RIDE_DISPATCH_PROBLEMS));
            canExecuteResult.sendMessageTo(messageAgent);
            return false;
        }

        for(DispatchTrigger dispatchTrigger : dispatchTriggerList){
            dispatchTrigger.execute(messageAgent);
        }

        return true;
    }

    @Override
    public Result canExecute(MessageAgent messageAgent) {
        Result result = null;

        for(DispatchTrigger dispatchTrigger : dispatchTriggerList){
            Result canExecuteResult = dispatchTrigger.canExecute(messageAgent);

            if(!canExecuteResult.ok()){
                if(result == null){
                    result = canExecuteResult;
                }else{
                    result.concat(canExecuteResult);
                }
            }
        }

        if(result == null)
            result = Result.isOk();

        return result;
    }

    @Override
    public void reset(){
        for(DispatchTrigger dispatchTrigger : dispatchTriggerList){
            dispatchTrigger.reset();
        }
    }

    @Override
    public boolean isActive(){
        return dispatchTriggerList.stream().anyMatch(DispatchTrigger::isActive);
    }

    @Override
    public DispatchLockCollection getDispatchLockCollection() {
        return dispatchLockCollection;
    }
}
