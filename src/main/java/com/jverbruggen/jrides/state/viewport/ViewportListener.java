package com.jverbruggen.jrides.state.viewport;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.event.player.PlayerSitDownEvent;
import com.jverbruggen.jrides.event.player.PlayerStandUpEvent;
import com.jverbruggen.jrides.event.ride.OnrideMusicTriggerEvent;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.state.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
        Location fromLocation = event.getFrom();
        Location toLocation = event.getTo();
        boolean xModRotated = (int)(toLocation.getX() / chunkSize) != (int)(fromLocation.getX() / chunkSize);
        boolean zModRotated = (int)(toLocation.getZ() / chunkSize) != (int)(fromLocation.getZ() / chunkSize);

        if(xModRotated || zModRotated){
            Player player = playerManager.getPlayer(event.getPlayer());
            viewportManager.updateVisuals(player);
        }
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
