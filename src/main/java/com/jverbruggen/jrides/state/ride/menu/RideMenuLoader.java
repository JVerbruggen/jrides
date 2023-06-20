package com.jverbruggen.jrides.state.ride.menu;

import com.jverbruggen.jrides.event.ride.RideInitializedEvent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RideMenuLoader implements Listener {
    private final RideOverviewMenuFactory rideOverviewMenuFactory;
    private final RideManager rideManager;

    public RideMenuLoader(){
        rideOverviewMenuFactory = ServiceProvider.getSingleton(RideOverviewMenuFactory.class);
        rideManager = ServiceProvider.getSingleton(RideManager.class);
    }

    @EventHandler
    public void onRidesInitialized(RideInitializedEvent event){
        rideOverviewMenuFactory.createRideOverviewMenu(rideManager.getRideHandles());
    }
}
