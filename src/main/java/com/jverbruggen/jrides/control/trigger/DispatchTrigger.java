package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
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
    public boolean execute(MessageReceiver messageReceiver){
        if(!dispatchLockCollection.allUnlocked()){
            if(messageReceiver != null){
                languageFile.sendMessage(messageReceiver, languageFile.notificationRideDispatchProblems, FeedbackType.CONFLICT);
                dispatchLockCollection.getProblems(Integer.MAX_VALUE)
                        .forEach(p -> languageFile.sendMessage(messageReceiver, p));
            }
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
