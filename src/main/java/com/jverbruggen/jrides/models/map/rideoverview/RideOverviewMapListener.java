package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.event.ride.RideInitializedEvent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RideOverviewMapListener implements Listener {
    private final RideOverviewMapFactory rideOverviewMapFactory;

    public RideOverviewMapListener() {
        this.rideOverviewMapFactory = ServiceProvider.getSingleton(RideOverviewMapFactory.class);
    }

    @EventHandler
    public void onRidesInitialized(RideInitializedEvent e){
        rideOverviewMapFactory.initializeMaps();
    }
}
