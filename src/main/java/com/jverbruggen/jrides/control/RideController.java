package com.jverbruggen.jrides.control;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.controlmode.SemiAutomaticMode;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.ride.Ride;

public class RideController {
    private RideHandle rideHandle;
    private TriggerContext triggerContext;
    private ControlMode controlMode;

    public RideController() {
        this.controlMode = new SemiAutomaticMode();
    }

    public void setRideHandle(RideHandle rideHandle) {
        this.rideHandle = rideHandle;
        this.triggerContext = new TriggerContext(
                rideHandle.getDispatchTrigger(),
                null,
                null);
    }

    public TriggerContext getTriggerContext() {
        return triggerContext;
    }

    public void changeMode(ControlMode newControlMode){
        if(controlMode != null) controlMode.stopOperating();

        newControlMode.setTriggerContext(triggerContext);
        controlMode = newControlMode;
        newControlMode.startOperating();
    }

    public Ride getRide() {
        return rideHandle.getRide();
    }
}
