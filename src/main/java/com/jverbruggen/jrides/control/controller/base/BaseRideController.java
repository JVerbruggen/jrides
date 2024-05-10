package com.jverbruggen.jrides.control.controller.base;

import com.jverbruggen.jrides.control.controller.RideController;

public abstract class BaseRideController implements RideController {
    private boolean active;
    protected boolean supportsMenu;

    public BaseRideController() {
        this.active = false;
        this.supportsMenu = true;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean supportsMenu() {
        return supportsMenu;
    }
}
