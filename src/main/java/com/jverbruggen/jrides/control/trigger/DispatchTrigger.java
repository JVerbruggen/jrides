package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import org.bukkit.Bukkit;

public class DispatchTrigger {
    private boolean active;
    private DispatchLockCollection dispatchLockCollection;

    public DispatchTrigger(DispatchLockCollection dispatchLockCollection) {
        this.active = false;
        this.dispatchLockCollection = dispatchLockCollection;
    }

    public boolean dispatch(){
        if(!dispatchLockCollection.allUnlocked()){
            Bukkit.broadcastMessage("Cannot dispatch due to the following problems:");
            dispatchLockCollection.getProblems().forEach(Bukkit::broadcastMessage);
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
}
