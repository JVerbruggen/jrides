package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

import java.util.List;

public class MultiDispatchTrigger implements DispatchTrigger {
    private boolean active;
    private final LanguageFile languageFile;
    private final List<DispatchTrigger> dispatchTriggerList;
    private final DispatchLockCollection dispatchLockCollection;

    public MultiDispatchTrigger(List<DispatchTrigger> dispatchTriggerList, DispatchLockCollection dispatchLockCollection) {
        this.active = false;
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.dispatchTriggerList = dispatchTriggerList;
        this.dispatchLockCollection = dispatchLockCollection;
    }

    @Override
    public boolean execute(MessageAgent messageAgent){
        if(!canExecute(messageAgent))
            return false;

        active = true;
        return true;
    }

    @Override
    public boolean canExecute(MessageAgent messageAgent) {
        boolean canExecute = true;

        for(DispatchTrigger dispatchTrigger : dispatchTriggerList){
            if(!dispatchTrigger.canExecute(messageAgent))
                canExecute = false;
        }

        return canExecute;
    }

    public void reset(){
        active = false;
    }

    public boolean isActive(){
        return active;
    }

    public DispatchLockCollection getDispatchLockCollection() {
        return dispatchLockCollection;
    }
}
