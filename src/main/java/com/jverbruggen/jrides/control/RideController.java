package com.jverbruggen.jrides.control;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.controlmode.factory.ControlModeFactory;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import javax.annotation.Nonnull;

public class RideController {
    private final ControlModeFactory controlModeFactory;
    private final StationHandle stationHandle;
    private RideHandle rideHandle;
    private ControlMode controlMode;
    private boolean active;

    public RideController(ControlModeFactory controlModeFactory, StationHandle stationHandle) {
        this.active = false;
        this.stationHandle = stationHandle;
        this.controlModeFactory = controlModeFactory;
        changeMode(this.controlModeFactory.getForWithoutOperating(this.stationHandle));
    }

    public void setRideHandle(RideHandle rideHandle) {
        this.rideHandle = rideHandle;

        if(this.controlMode == null) return;
        this.controlMode.setTriggerContext(getTriggerContext());
        active = true;
    }

    public TriggerContext getTriggerContext() {
        return rideHandle.getTriggerContext(null);
    }

    public void changeMode(ControlMode newControlMode){
        if(newControlMode == null){
            active = false;
        }else{
            if(rideHandle != null){
                newControlMode.setTriggerContext(getTriggerContext());
                active = true;
            }
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
        Player previousOperator = this.getControlMode().getOperator();
        if(previousOperator == player) return true;

        if(player == null){
            this.changeMode(this.controlModeFactory.getForWithoutOperating(this.stationHandle));
        }else if(previousOperator == null){
            this.changeMode(this.controlModeFactory.getForWithOperator(this.stationHandle));
        }
        return this.getControlMode().setOperator(player);
    }

    public Player getOperator(){
        return this.getControlMode().getOperator();
    }

    public boolean isActive() {
        return active;
    }
}
