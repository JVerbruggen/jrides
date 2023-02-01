package com.jverbruggen.jrides.state.viewport;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ViewportListener implements Listener {
    private final int chunkSize = 8;
    private final ViewportManager viewportManager;
    private final PlayerManager playerManager;

    public ViewportListener(ViewportManager viewportManager, PlayerManager playerManager) {
        this.viewportManager = viewportManager;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(Vector3.chunkRotated(event.getFrom(), event.getTo(), 8)){
            Player player = playerManager.getPlayer(event.getPlayer());
            viewportManager.updateVisuals(player);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event){
        Player player = playerManager.getPlayer(event.getPlayer());
        viewportManager.updateVisuals(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = playerManager.getPlayer(event.getPlayer());

        Bukkit.getScheduler().runTaskLater(
                JRidesPlugin.getBukkitPlugin(),
                () -> viewportManager.updateVisuals(player),
                5L);
    }

}
