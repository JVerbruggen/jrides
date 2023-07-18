package com.jverbruggen.jrides.event.player;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.api.JRidesRide;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class PlayerFinishedRideEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final JRidesPlayer player;
    private final JRidesRide ride;

    public PlayerFinishedRideEvent(JRidesPlayer player, JRidesRide ride) {
        this.player = player;
        this.ride = ride;
    }

    public JRidesPlayer getPlayer() {
        return player;
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

    public static void sendFinishedRideEvent(List<JRidesPlayer> players, JRidesRide ride){
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        players.forEach(p -> pluginManager.callEvent(new PlayerFinishedRideEvent(p, ride)));
    }

}
