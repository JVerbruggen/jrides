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

package com.jverbruggen.jrides.api;

import com.jverbruggen.jrides.common.MenuSessionManager;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.menu.RideOverviewMenuFactory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class PlayerAPI {
    private final PlayerManager playerManager;
    private final RideOverviewMenuFactory rideOverviewMenuFactory;
    private final MenuSessionManager menuSessionManager;

    public PlayerAPI() {
        playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        rideOverviewMenuFactory = ServiceProvider.getSingleton(RideOverviewMenuFactory.class);
        menuSessionManager = ServiceProvider.getSingleton(MenuSessionManager.class);
    }

    public JRidesPlayer getFromBukkitPlayer(org.bukkit.entity.Player bukkitPlayer){
        return playerManager.getPlayer(bukkitPlayer);
    }

    public void displayRideOverviewMenu(JRidesPlayer player){
        Menu menu = rideOverviewMenuFactory.getRideOverviewMenu();
        Inventory inventory = menu.getInventoryFor(player);

        menuSessionManager.addOpenMenu(player, menu, inventory);
        player.getBukkitPlayer().openInventory(inventory);
    }

    public static PlayerAPI Connect(){
        return Bukkit.getServicesManager().load(PlayerAPI.class);
    }
}
