package com.jverbruggen.jrides.control.controller.base;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.controlmode.factory.ControlModeFactory;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

public abstract class SingularRideController extends BaseRideController implements RideController {
    private final ControlModeFactory controlModeFactory;
    private ControlMode controlMode;
    private RideHandle rideHandle;

    protected SingularRideController() {
        this.controlModeFactory = ServiceProvider.getSingleton(ControlModeFactory.class);
    }

    @Override
    public RideHandle getRideHandle() {
        return rideHandle;
    }

    @Override
    public TriggerContext getTriggerContext() {
        return getRideHandle().getTriggerContext(null);
    }

    @Override
    public void setRideHandle(RideHandle rideHandle) {
        this.rideHandle = rideHandle;
    }

    @Override
    public Ride getRide() {
        return rideHandle.getRide();
    }

    @Override
    public ControlMode getControlMode() {
        return controlMode;
    }

    @Override
    public void setControlMode(ControlMode controlMode) {
        this.controlMode = controlMode;
    }

    @Override
    public boolean setOperator(Player player){
        Player previousOperator = this.getControlMode().getOperator();
        if(previousOperator == player) return true;

        if(player == null){
            this.changeMode(this.controlModeFactory.getForWithoutOperator(this.rideHandle));
        }else if(previousOperator == null){
            this.changeMode(this.controlModeFactory.getForWithOperator(this.rideHandle));
        }
        return this.getControlMode().setOperator(player);
    }

    @Override
    public Player getOperator(){
        return this.getControlMode().getOperator();
    }

    public ControlModeFactory getControlModeFactory() {
        return controlModeFactory;
    }
}
