package com.jverbruggen.jrides.models.ride.count;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.UnloadedRide;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class RideCounterRecord implements ConfigurationSerializable {
    private Ride ride;
    private int rideCount;

    public RideCounterRecord(Ride ride, int rideCount) {
        this.ride = ride;
        this.rideCount = rideCount;
    }

    public boolean isActive(){
        return this.ride.isLoaded();
    }

    public Ride getRide() {
        return ride;
    }

    public String getRideIdentifier(){
        return ride.getIdentifier();
    }

    public int getRideCount() {
        return rideCount;
    }

    public void addOne(){
        rideCount++;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> config = new HashMap<>();

        config.put("rideIdentifier", this.ride.getIdentifier());
        config.put("rideCount", this.rideCount);

        return config;
    }

    public static RideCounterRecord deserialize(Map<String, Object> config){
        String rideIdentifier = (String) config.get("rideIdentifier");

        RideHandle rideHandle = ServiceProvider.getSingleton(RideManager.class)
                .getRideHandle(rideIdentifier);
        Ride ride;
        if(rideHandle != null) ride = rideHandle.getRide();
        else ride = new UnloadedRide(rideIdentifier);

        int rideCount = (int) config.get("rideCount");

        return new RideCounterRecord(ride, rideCount);
    }
}
