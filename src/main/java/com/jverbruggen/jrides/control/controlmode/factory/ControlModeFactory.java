package com.jverbruggen.jrides.control.controlmode.factory;

import com.jverbruggen.jrides.control.controlmode.AutomaticMode;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.controlmode.SemiAutomaticMode;
import com.jverbruggen.jrides.models.ride.StationHandle;

public class ControlModeFactory {
    public ControlMode getForWithOperator(StationHandle stationHandle){
        if(stationHandle == null) return null;
        return new SemiAutomaticMode(
                stationHandle,
                stationHandle.getCoasterHandle().getDispatchTrigger().getDispatchLockCollection());
    }

    public ControlMode getForWithoutOperating(StationHandle stationHandle){
        if(stationHandle == null) return null;
        return new AutomaticMode(
                stationHandle,
                stationHandle.getCoasterHandle().getDispatchTrigger().getDispatchLockCollection());
    }
}
