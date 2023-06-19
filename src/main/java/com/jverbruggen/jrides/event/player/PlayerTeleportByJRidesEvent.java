package com.jverbruggen.jrides.event.player;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.api.JRidesPlayerLocation;
import com.jverbruggen.jrides.api.JRidesRide;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public class PlayerTeleportByJRidesEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final JRidesPlayer player;
    private final JRidesPlayerLocation location;
    private boolean cancelled;

    public PlayerTeleportByJRidesEvent(JRidesPlayer player, JRidesPlayerLocation location) {
        this.player = player;
        this.location = location;
        this.cancelled = false;
    }

    public JRidesPlayer getPlayer() {
        return player;
    }

    public JRidesPlayerLocation getLocation() {
        return location;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void sendEvent(JRidesPlayer player, JRidesPlayerLocation playerLocation){
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        pluginManager.callEvent(new PlayerTeleportByJRidesEvent(player, playerLocation));
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}