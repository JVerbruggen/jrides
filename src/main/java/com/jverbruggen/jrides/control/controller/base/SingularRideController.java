package com.jverbruggen.jrides.control.controller.base;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.controlmode.factory.ControlModeFactory;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
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
        return getRideHandle().getFirstTriggerContext();
    }

    @Override
    public void setRideHandle(RideHandle rideHandle) {
        this.rideHandle = rideHandle;

        if(getControlMode() == null) return;
        getControlMode().setTriggerContext(getTriggerContext());
        setActive(true);
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
    public void changeMode(ControlMode newControlMode){
        if(newControlMode == null){
            JRidesPlugin.getLogger().severe("Control mode of ride controller of " + getRide().getIdentifier() + " was set to null. Ride is now inactive");
            setActive(false);
        }else{
            if(getRideHandle() != null){
                newControlMode.setTriggerContext(getTriggerContext());
                setActive(true);
            }
        }

        setControlMode(newControlMode);
    }

    @Override
    public Player getOperator(){
        return this.getControlMode().getOperator();
    }

    public ControlModeFactory getControlModeFactory() {
        return controlModeFactory;
    }


    @Override
    public void onTrainArrive(Train train, StationHandle stationHandle){
        getControlMode().onVehicleArrive(train, stationHandle);
    }

    @Override
    public void onTrainDepart(Train train, StationHandle stationHandle) {
        getControlMode().onVehicleDepart(train, stationHandle);
    }
}
