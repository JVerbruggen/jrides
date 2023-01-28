package com.jverbruggen.jrides.event.player;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class PlayerFinishedRideEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final org.bukkit.entity.Player player;
    private final String rideIdentifier;

    public PlayerFinishedRideEvent(org.bukkit.entity.Player player, String rideIdentifier) {
        this.player = player;
        this.rideIdentifier = rideIdentifier;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void sendFinishedRideEvent(List<Player> players, Ride ride){
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        players.forEach(p -> pluginManager.callEvent(new PlayerFinishedRideEvent(p.getBukkitPlayer(), ride.getIdentifier())));
    }

}
