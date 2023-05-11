package com.jverbruggen.jrides.control.controller;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controller.base.SingularRideController;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.controlmode.factory.ControlModeFactory;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class SimpleRideController extends SingularRideController implements RideController {
    private final CoasterStationHandle stationHandle;

    public SimpleRideController(CoasterStationHandle stationHandle, RideHandle rideHandle) {
        super();
        this.setActive(false);
        this.stationHandle = stationHandle;
        this.changeMode(this.getControlModeFactory().getForWithoutOperator(rideHandle));

        this.setRideHandle(rideHandle);
    }

    @Override
    public void setRideHandle(RideHandle rideHandle) {
        super.setRideHandle(rideHandle);

        if(getControlMode() == null) return;
        getControlMode().setTriggerContext(getTriggerContext());
        setActive(true);
    }

    @Override
    public TriggerContext getTriggerContext() {
        return getRideHandle().getTriggerContext(null);
    }

    @Override
    public void changeMode(ControlMode newControlMode){
        if(newControlMode == null){
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
    public void onTrainArrive(Train train, StationHandle stationHandle){
        getControlMode().onVehicleArrive(train, stationHandle);
    }

    @Override
    public void onTrainDepart(Train train, StationHandle stationHandle) {
        getControlMode().onVehicleDepart(train, stationHandle);
    }
}
