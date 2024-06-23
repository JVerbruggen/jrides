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

import com.jverbruggen.jrides.api.JRidesRide;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public class RideStateUpdatedEvent extends Event {
    /**
     * Sent when system is done initializing all rides
     */
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final JRidesRide ride;
    private final RideState newState;

    public RideStateUpdatedEvent(JRidesRide ride, RideState newState) {
        this.ride = ride;
        this.newState = newState;
    }

    public JRidesRide getRide() {
        return ride;
    }

    public RideState getNewState() {
        return newState;
    }

    public RideStateUpdatedEvent(boolean isAsync, JRidesRide ride, RideState newState) {
        super(isAsync);
        this.ride = ride;
        this.newState = newState;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static void send(JRidesRide ride, RideState newState){
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        pluginManager.callEvent(new RideStateUpdatedEvent(ride, newState));
    }
}
