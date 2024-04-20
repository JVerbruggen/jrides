package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.common.Result;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

public class SimpleDispatchTrigger implements DispatchTrigger {
    private boolean active;
    private final DispatchLockCollection dispatchLockCollection;
    private final LanguageFile languageFile;

    public SimpleDispatchTrigger(DispatchLockCollection dispatchLockCollection) {
        this.active = false;
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

        active = true;
        return true;
    }

    @Override
    public Result canExecute(MessageAgent messageAgent) {
        if(!dispatchLockCollection.allUnlocked()){
            if(messageAgent != null){
                return Result.isNotOk(dispatchLockCollection.getProblems(Integer.MAX_VALUE, messageAgent.hasPermission(Permissions.ELEVATED_DISPATCH_PROBLEMS_DEBUG)));
            }else{
                return Result.isNotOk();
            }
        }
        if(messageAgent != null && !messageAgent.hasPermission(Permissions.CABIN_OPERATE)){
            return Result.isNotOk(languageFile.get(LanguageFileField.ERROR_OPERATING_NO_PERMISSION));
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
}
