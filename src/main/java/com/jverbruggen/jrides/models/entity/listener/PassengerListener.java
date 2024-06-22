package com.jverbruggen.jrides.models.entity.listener;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PassengerListener implements Listener {
    private final PlayerManager playerManager;

    public PassengerListener(){
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
    }

    @EventHandler
    public void onPlayerFly(PlayerToggleFlightEvent e) {
        org.bukkit.entity.Player bukkitPlayer = e.getPlayer();
        Player player = playerManager.getPlayer(bukkitPlayer);
        if(!player.isSeated()) return;

        e.setCancelled(true);
        bukkitPlayer.setFlying(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        org.bukkit.entity.Player bukkitPlayer = e.getPlayer();
        Player player = playerManager.getPlayer(bukkitPlayer);
        if (!player.isSeated()) return;

        player.getSeatedOnContext().restore(player);
    }
}
