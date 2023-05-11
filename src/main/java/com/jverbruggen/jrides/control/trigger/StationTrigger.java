package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;

public interface StationTrigger extends Trigger {
    void setStationHandle(CoasterStationHandle stationHandle);
    DispatchLock getLock();
}
