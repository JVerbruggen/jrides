package com.jverbruggen.jrides.control.controlmode.advanced;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controlmode.BaseControlMode;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.List;

public class ProxyControlMode extends BaseControlMode implements ControlMode {
    private List<ControlMode> targets;
    private List<StationHandle> stationHandles;

    public ProxyControlMode(RideHandle rideHandle, MinMaxWaitingTimer waitingTimer, List<StationHandle> stationHandles) {
        super(rideHandle, null,
                waitingTimer,
                null,
                true);

        this.stationHandles = stationHandles;
    }

    @Override
    protected void incrementWaitingTimer() {
        stationHandles.forEach(s -> s.getWaitingTimer().increment(tickInterval));
    }

    @Override
    public StationHandle getStationHandle() {
        throw new RuntimeException("Cannot get station handle in proxy control mode");
    }

    @Override
    public void onVehicleArrive(Train train, StationHandle stationHandle) {
        targets.stream().filter(t -> t.getStationHandle() == stationHandle)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Station handle not found on vehicle arrive!"))
                .onVehicleArrive(train, stationHandle);
    }

    @Override
    public void onVehicleDepart(Train train, StationHandle stationHandle) {
        targets.stream().filter(t -> t.getStationHandle() == stationHandle)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Station handle not found on vehicle depart!"))
                .onVehicleDepart(train, stationHandle);
    }
}
