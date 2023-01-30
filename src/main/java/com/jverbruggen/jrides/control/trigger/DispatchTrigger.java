package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import org.bukkit.ChatColor;

public class DispatchTrigger implements Trigger {
    private boolean active;
    private DispatchLockCollection dispatchLockCollection;

    public DispatchTrigger(DispatchLockCollection dispatchLockCollection) {
        this.active = false;
        this.dispatchLockCollection = dispatchLockCollection;
    }

    @Override
    public boolean execute(MessageReceiver messageReceiver){
        if(!dispatchLockCollection.allUnlocked()){
            if(messageReceiver != null){
                messageReceiver.sendMessage(ChatColor.RED + "Cannot dispatch due to the following problems:");
                dispatchLockCollection.getProblems(Integer.MAX_VALUE).forEach(messageReceiver::sendMessage);
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
