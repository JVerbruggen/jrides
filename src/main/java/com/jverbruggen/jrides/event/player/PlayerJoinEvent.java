package com.jverbruggen.jrides.event.player;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public class PlayerJoinEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final JRidesPlayer player;

    public PlayerJoinEvent(JRidesPlayer player) {
        this.player = player;
    }

    public JRidesPlayer getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void send(JRidesPlayer player){
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(),
                () -> pluginManager.callEvent(new PlayerJoinEvent(player)));
    }
}
