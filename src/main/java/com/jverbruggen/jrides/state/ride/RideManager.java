package com.jverbruggen.jrides.state.ride;

import com.jverbruggen.jrides.animator.GCRideHandle;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.identifier.RideIdentifier;

import java.util.ArrayList;
import java.util.List;

public class RideManager {
    private List<GCRideHandle> rideHandles;

    public RideManager() {
        this.rideHandles = new ArrayList<>();
    }

    public Ride GetRide(RideIdentifier identifier){
        return null;
    }

    public void addRideHandle(GCRideHandle rideHandle){
        rideHandles.add(rideHandle);
    }

    public GCRideHandle getRideHandle(String identifier){
        return this.rideHandles.get(0);
    }
}
