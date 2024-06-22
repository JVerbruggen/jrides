package com.jverbruggen.jrides.state.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerManagerListener implements Listener {
    private PlayerManager playerManager;

    public PlayerManagerListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        playerManager.removePlayer(event.getPlayer());
    }
}
