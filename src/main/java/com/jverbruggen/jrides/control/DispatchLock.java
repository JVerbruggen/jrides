package com.jverbruggen.jrides.control;

public class DispatchLock {
    private DispatchLockCollection parentCollection;
    private boolean locked;
    private String description;

    public DispatchLock(DispatchLockCollection parentCollection, String description) {
        this.parentCollection = parentCollection;
        this.description = description;
        this.locked = true;

        this.parentCollection.addDispatchLock(this);
    }

    public String getDescription() {
        return description;
    }

    public void lock(){
        if(locked) return;
        locked = true;
        parentCollection.onLock(this);
    }

    public void unlock(){
        if(!locked) return;
        locked = false;
        parentCollection.onUnlock(this);
    }

    public boolean isUnlocked(){
        return !locked;
    }
}
