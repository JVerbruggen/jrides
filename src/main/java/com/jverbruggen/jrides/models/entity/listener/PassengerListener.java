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
        player.setFly(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        org.bukkit.entity.Player bukkitPlayer = e.getPlayer();
        Player player = playerManager.getPlayer(bukkitPlayer);
        if (!player.isSeated()) return;

        player.getSeatedOnContext().restore(player);
    }
}
