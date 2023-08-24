package com.jverbruggen.jrides.event.ride;

import com.jverbruggen.jrides.api.JRidesRide;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import java.util.List;


public class RideFinishedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final JRidesRide ride;
    private final List<Player> players;

    public RideFinishedEvent(JRidesRide ride, List<Player> players) {
        this.ride = ride;
        this.players = players;
    }

    public JRidesRide getRide() {
        return ride;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void send(JRidesRide ride, List<Player> players) {
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        pluginManager.callEvent(new RideFinishedEvent(ride, players));
    }
}
