package com.jverbruggen.jrides.control;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class RideController {
    private RideHandle rideHandle;
    private ControlMode controlMode;

    public RideController(ControlMode controlMode) {
        changeMode(controlMode);
    }

    public void setRideHandle(RideHandle rideHandle) {
        this.rideHandle = rideHandle;
        this.controlMode.setTriggerContext(getTriggerContext());
    }

    public TriggerContext getTriggerContext() {
        return rideHandle.getTriggerContext(null);
    }

    public void changeMode(ControlMode newControlMode){
        if(rideHandle != null){
            newControlMode.setTriggerContext(getTriggerContext());
        }
        controlMode = newControlMode;
    }

    public Ride getRide() {
        return rideHandle.getRide();
    }

    public void onTrainArrive(Train train){
        controlMode.onTrainArrive(train);
    }

    public void onTrainDepart(Train train) {
        controlMode.onTrainDepart(train);
    }

    public ControlMode getControlMode() {
        return controlMode;
    }

    public boolean setOperator(Player player){
        return this.getControlMode().setOperator(player);
    }

    public Player getOperator(){
        return this.getControlMode().getOperator();
    }
}
