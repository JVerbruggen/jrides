/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.state.viewport;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;

public class ViewportListener implements Listener {
    private final int chunkSize = 8;
    private final ViewportManager viewportManager;
    private final PlayerManager playerManager;

    public ViewportListener(ViewportManager viewportManager, PlayerManager playerManager) {
        this.viewportManager = viewportManager;
        this.playerManager = playerManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event){
        if(Vector3.chunkRotated(event.getFrom(), event.getTo(), 8)){
            Player player = playerManager.getPlayer(event.getPlayer());
            viewportManager.updateVisuals(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleport(PlayerTeleportEvent event){
        Player player = playerManager.getPlayer(event.getPlayer());
        viewportManager.updateVisuals(player, Objects.requireNonNullElse(Vector3.fromBukkitLocation(event.getTo()), player.getLocation()));

        if(player.isSeated()){
            player.getSeatedOn().setPassenger(null);
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
