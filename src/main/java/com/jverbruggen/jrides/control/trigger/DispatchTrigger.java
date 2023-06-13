package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileFields;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

public class DispatchTrigger implements Trigger {
    private boolean active;
    private DispatchLockCollection dispatchLockCollection;
    private final LanguageFile languageFile;

    public DispatchTrigger(DispatchLockCollection dispatchLockCollection) {
        this.active = false;
        this.dispatchLockCollection = dispatchLockCollection;
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    @Override
    public boolean execute(MessageAgent messageAgent){
        if(!dispatchLockCollection.allUnlocked()){
            if(messageAgent != null){
                languageFile.sendMessage(messageAgent, LanguageFileFields.NOTIFICATION_RIDE_DISPATCH_PROBLEMS, FeedbackType.CONFLICT);
                dispatchLockCollection.getProblems(Integer.MAX_VALUE)
                        .forEach(p -> languageFile.sendMessage(messageAgent, p));
            }
            return false;
        }
        if(messageAgent != null && !messageAgent.hasPermission(Permissions.CABIN_OPERATE)){
            languageFile.sendMessage(messageAgent, LanguageFileFields.ERROR_OPERATING_NO_PERMISSION);
            return false;
        }

        active = true;

        return true;
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
