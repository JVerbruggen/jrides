package com.jverbruggen.jrides.models.map.ridecounter;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.event.ride.RideFinishedEvent;
import com.jverbruggen.jrides.event.ride.RideInitializedEvent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;
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

    @EventHandler
    public void onRideFinished(RideFinishedEvent e) {
        // Add a small delay before updating the map, to make sure the ridecounters are updated
        Bukkit.getScheduler().runTaskLater(JRidesPlugin.getBukkitPlugin(),
                () -> {
                    rideCounterMapFactory.updateMapsByRide(e.getRide().getIdentifier());
                },
                10L);
    }

}
