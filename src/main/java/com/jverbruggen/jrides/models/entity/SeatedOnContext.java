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

package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.common.Sync;
import com.jverbruggen.jrides.models.ride.seat.Seat;

enum SeatedOnState {
    CREATED,
    SETUP,
    RESTORED
}

public class SeatedOnContext {
    private SeatedOnState state;
    private final Seat seat;
    private final boolean wasFlying;
    private final boolean wasAllowedFlying;

    public SeatedOnContext(Seat seat, boolean wasFlying, boolean wasAllowedFlying) {
        this.state = SeatedOnState.CREATED;
        this.seat = seat;
        this.wasFlying = wasFlying;
        this.wasAllowedFlying = wasAllowedFlying;
    }

    public static SeatedOnContext create(Seat seat, Player player){
        org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();
        boolean wasFlying = bukkitPlayer.isFlying();
        boolean wasAllowedFlying = bukkitPlayer.getAllowFlight();

        return new SeatedOnContext(seat, wasFlying, wasAllowedFlying);
    }

    public void setup(Player player){
        if(state != SeatedOnState.CREATED) throw new RuntimeException("Already set up context");

        org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();

        Sync.runSynced(() -> {
            player.setFly(true);

            player.setSeatedOnContext(this);
            state = SeatedOnState.SETUP;
        });
    }

    public void restore(Player player){
        if(state != SeatedOnState.SETUP) throw new RuntimeException("Already restored context");

        Sync.runSynced(() -> {
            player.setFly(wasFlying, wasAllowedFlying);

            player.clearSmoothAnimationRotation();

            player.setSeatedOnContext(null);
            state = SeatedOnState.RESTORED;
        });
    }

    public Seat getSeat() {
        return seat;
    }
}
