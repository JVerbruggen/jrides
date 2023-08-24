package com.jverbruggen.jrides.event.ride;

import com.jverbruggen.jrides.api.JRidesRide;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;


public class RideFinishedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final JRidesRide ride;

    public RideFinishedEvent(JRidesRide ride) {
        this.ride = ride;
    }

    public JRidesRide getRide() {
        return ride;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void send(JRidesRide ride) {
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        pluginManager.callEvent(new RideFinishedEvent(ride));
    }
}
