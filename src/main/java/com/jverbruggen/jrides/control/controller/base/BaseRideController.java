package com.jverbruggen.jrides.control.controller.base;

import com.jverbruggen.jrides.control.controller.RideController;

public abstract class BaseRideController implements RideController {
    private boolean active;

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
