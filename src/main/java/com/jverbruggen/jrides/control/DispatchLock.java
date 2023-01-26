package com.jverbruggen.jrides.control;

public class DispatchLock {
    private boolean locked;
    private String name;

    public DispatchLock(String name) {
        this.name = name;
        this.locked = true;
    }

    public String getName() {
        return name;
    }

    public void lock(){
        locked = true;
    }

    public void unlock(){
        locked = false;
    }

    public boolean isUnlocked(){
        return !locked;
    }
}
