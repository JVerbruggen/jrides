package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.models.ride.StationHandle;

public interface StationTrigger extends Trigger {
    void setStationHandle(StationHandle stationHandle);
}
