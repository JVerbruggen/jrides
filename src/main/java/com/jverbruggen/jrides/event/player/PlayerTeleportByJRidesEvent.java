package com.jverbruggen.jrides.event.player;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.api.JRidesPlayerLocation;
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
    private boolean hard;

    public PlayerTeleportByJRidesEvent(JRidesPlayer player, JRidesPlayerLocation location, boolean hard) {
        this.player = player;
        this.location = location;
        this.cancelled = false;
        this.hard = hard;
    }

    public JRidesPlayer getPlayer() {
        return player;
    }

    public JRidesPlayerLocation getLocation() {
        return location;
    }

    public boolean isHardTeleport() {
        return hard;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void sendEvent(JRidesPlayer player, JRidesPlayerLocation playerLocation, boolean hard){
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        pluginManager.callEvent(new PlayerTeleportByJRidesEvent(player, playerLocation, hard));
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
