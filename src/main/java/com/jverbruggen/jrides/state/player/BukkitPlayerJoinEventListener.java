package com.jverbruggen.jrides.state.player;

import com.jverbruggen.jrides.event.player.PlayerJoinEvent;
import com.jverbruggen.jrides.event.player.PlayerQuitEvent;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BukkitPlayerJoinEventListener implements Listener {
    private final PlayerManager playerManager;

    public BukkitPlayerJoinEventListener() {
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent bukkitEvent){
        Player player = playerManager.registerPlayer(bukkitEvent.getPlayer());
        PlayerJoinEvent.send(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(org.bukkit.event.player.PlayerJoinEvent bukkitEvent){
        // TODO: Is this correct???
        Player player = playerManager.registerPlayer(bukkitEvent.getPlayer());
        PlayerQuitEvent.send(player);
    }
}
