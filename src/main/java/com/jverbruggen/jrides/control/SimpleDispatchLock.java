package com.jverbruggen.jrides.control;

public class SimpleDispatchLock implements DispatchLock {
    private DispatchLockCollection parentCollection;
    private boolean locked;
    private String description;

    public SimpleDispatchLock(DispatchLockCollection parentCollection, String description) {
        this.parentCollection = parentCollection;
        this.description = description;
        this.locked = true;

        this.parentCollection.addDispatchLock(this);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void lock(){
        if(locked) return;
        locked = true;
        parentCollection.onLock(this);
    }

    @Override
    public void unlock(){
        if(!locked) return;
        locked = false;
        parentCollection.onUnlock(this);
    }

    @Override
    public boolean isUnlocked(){
        return !locked;
    }

    @Override
    public void setLocked(boolean locked) {
        if(locked) lock();
        else unlock();
    }
}
