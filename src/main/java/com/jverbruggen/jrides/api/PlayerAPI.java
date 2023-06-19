package com.jverbruggen.jrides.api;

import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;

public class PlayerAPI {
    private final PlayerManager playerManager;

    public PlayerAPI() {
        playerManager = ServiceProvider.getSingleton(PlayerManager.class);
    }

    public JRidesPlayer getFromBukkitPlayer(org.bukkit.entity.Player bukkitPlayer){
        return playerManager.getPlayer(bukkitPlayer);
    }
}
