package com.jverbruggen.jrides.control.controller;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controller.base.SingularRideController;
import com.jverbruggen.jrides.control.trigger.TriggerContext;

public class DualRideController extends SingularRideController implements RideController {
    private TriggerContext leftStationTriggerContext;
    private TriggerContext rightStationTriggerContext;

    public DualRideController(TriggerContext leftStationTriggerContext, TriggerContext rightStationTriggerContext, RideHandle rideHandle) {
        this.leftStationTriggerContext = leftStationTriggerContext;
        this.rightStationTriggerContext = rightStationTriggerContext;

        this.changeMode(this.getControlModeFactory().getForWithoutOperator(rideHandle));
        this.setRideHandle(rideHandle);
    }

    public TriggerContext getLeftStationTriggerContext() {
        return leftStationTriggerContext;
    }

    public TriggerContext getRightStationTriggerContext() {
        return rightStationTriggerContext;
    }
}
