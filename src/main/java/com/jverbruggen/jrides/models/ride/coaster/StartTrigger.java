package com.jverbruggen.jrides.models.ride.coaster;

public class StartTrigger {
    private boolean active;

    public StartTrigger() {
        this.active = false;
    }

    public void dispatch(){
        active = true;
    }

    public void reset(){
        active = false;
    }

    public boolean isActive(){
        return active;
    }
}
