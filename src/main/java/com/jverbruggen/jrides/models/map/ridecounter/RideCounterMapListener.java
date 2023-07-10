package com.jverbruggen.jrides.models.map.ridecounter;

import com.jverbruggen.jrides.event.ride.RideInitializedEvent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RideCounterMapListener implements Listener {

    private final RideCounterMapFactory rideCounterMapFactory;

    public RideCounterMapListener() {
        this.rideCounterMapFactory = ServiceProvider.getSingleton(RideCounterMapFactory.class);
    }

    @EventHandler
    public void onRidesInitialized(RideInitializedEvent e){
        rideCounterMapFactory.initializeMaps();
    }

}
