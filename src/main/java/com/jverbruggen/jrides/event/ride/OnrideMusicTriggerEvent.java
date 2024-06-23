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

package com.jverbruggen.jrides.event.ride;

import com.jverbruggen.jrides.api.JRidesPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class OnrideMusicTriggerEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final List<JRidesPlayer> players;
    private final String resource;
    private final String descriptor;

    public OnrideMusicTriggerEvent(List<JRidesPlayer> players, String resource, String descriptor) {
        this.players = players;
        this.resource = resource;
        this.descriptor = descriptor;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public List<JRidesPlayer> getPlayers(){
        return players;
    }

    public String getResource() {
        return resource;
    }

    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
