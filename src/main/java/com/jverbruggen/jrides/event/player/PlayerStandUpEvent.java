package com.jverbruggen.jrides.event.player;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public class PlayerStandUpEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final org.bukkit.entity.Player player;
    private final String rideIdentifier;

    public PlayerStandUpEvent(org.bukkit.entity.Player player, String rideIdentifier) {
        this.player = player;
        this.rideIdentifier = rideIdentifier;
    }

    public org.bukkit.entity.Player getPlayer() {
        return player;
    }

    public String getRideIdentifier() {
        return rideIdentifier;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void send(Player player, Ride ride){
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(),
                () -> pluginManager.callEvent(new PlayerStandUpEvent(player.getBukkitPlayer(), ride.getIdentifier())));
    }

}
