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
import com.jverbruggen.jrides.api.JRidesRide;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class PlayerFinishedRideEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final JRidesPlayer player;
    private final JRidesRide ride;

    public PlayerFinishedRideEvent(JRidesPlayer player, JRidesRide ride) {
        this.player = player;
        this.ride = ride;
    }

    public JRidesPlayer getPlayer() {
        return player;
    }

    public JRidesRide getRide() {
        return ride;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void sendFinishedRideEvent(List<JRidesPlayer> players, JRidesRide ride){
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        players.forEach(p -> pluginManager.callEvent(new PlayerFinishedRideEvent(p, ride)));
    }

}
