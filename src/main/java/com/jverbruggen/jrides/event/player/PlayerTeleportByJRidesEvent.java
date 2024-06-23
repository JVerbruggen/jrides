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

package com.jverbruggen.jrides.event.player;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.api.JRidesPlayerLocation;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public class PlayerTeleportByJRidesEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final JRidesPlayer player;
    private final JRidesPlayerLocation location;
    private boolean cancelled;
    private boolean hard;

    public PlayerTeleportByJRidesEvent(JRidesPlayer player, JRidesPlayerLocation location, boolean hard) {
        this.player = player;
        this.location = location;
        this.cancelled = false;
        this.hard = hard;
    }

    public JRidesPlayer getPlayer() {
        return player;
    }

    public JRidesPlayerLocation getLocation() {
        return location;
    }

    public boolean isHardTeleport() {
        return hard;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void sendEvent(JRidesPlayer player, JRidesPlayerLocation playerLocation, boolean hard){
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        pluginManager.callEvent(new PlayerTeleportByJRidesEvent(player, playerLocation, hard));
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
