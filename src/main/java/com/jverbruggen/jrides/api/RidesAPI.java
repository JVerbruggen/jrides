package com.jverbruggen.jrides.api;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;

import java.util.List;

public class RidesAPI {
    private final RideManager rideManager;

    public RidesAPI(){
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
    }

    public List<RideHandle> getRides(){
        return this.rideManager.getRideHandles();
    }

    public RideHandle getRide(String rideIdentifier){
        return this.rideManager.getRideHandle(rideIdentifier);
    }
}
