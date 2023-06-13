package com.jverbruggen.jrides.control.controlmode.factory;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controlmode.AutomaticMode;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.controlmode.SemiAutomaticMode;
import com.jverbruggen.jrides.control.controlmode.advanced.ProxyControlMode;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.StationHandle;

import java.util.List;

public class ControlModeFactory {
    public ControlMode getForWithOperator(RideHandle rideHandle){
        if(rideHandle == null) return null;

        List<StationHandle> stationHandles = rideHandle.getStationHandles();
        int size = stationHandles.size();
        if(size == 0)
            return null;
        else if(size == 1)
            return getSingleForWithOperator(rideHandle, stationHandles.get(0));
        else{
            throw new RuntimeException("No support for multiple stations yet for control mode ( with operator )");
        }
    }

    public ControlMode getForWithoutOperator(RideHandle rideHandle){
        if(rideHandle == null) return null;

        List<StationHandle> stationHandles = rideHandle.getStationHandles();
        int size = stationHandles.size();
        if(size == 0)
            return null;
        else if(size == 1)
            return getSingleForWithoutOperator(rideHandle, stationHandles.get(0));
        else{
            // For dispatching 1 station at a time
            return getAlternateForWithoutOperator(rideHandle, stationHandles);
        }
    }

    private ControlMode getSingleForWithOperator(RideHandle rideHandle, StationHandle stationHandle){
        if(stationHandle == null) return null;
        MinMaxWaitingTimer waitingTimer = stationHandle.getWaitingTimer();

        return new SemiAutomaticMode(
                rideHandle,
                stationHandle,
                waitingTimer,
                stationHandle.getTriggerContext().getDispatchTrigger().getDispatchLockCollection());
    }

    private ControlMode getSingleForWithoutOperator(RideHandle rideHandle, StationHandle stationHandle){
        if(stationHandle == null) return null;
        MinMaxWaitingTimer waitingTimer = stationHandle.getWaitingTimer();

        return new AutomaticMode(
                rideHandle,
                stationHandle,
                waitingTimer,
                stationHandle.getTriggerContext().getDispatchTrigger().getDispatchLockCollection());
    }

    private ControlMode getAlternateForWithoutOperator(RideHandle rideHandle, List<StationHandle> stationHandles){
        return new ProxyControlMode(rideHandle, stationHandles.get(0).getWaitingTimer(), stationHandles);
    }
}
