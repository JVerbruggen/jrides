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

package com.jverbruggen.jrides.animator.smoothanimation;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Quaternion;
import me.m56738.smoothcoasters.api.SmoothCoastersAPI;

public class SmoothCoastersSmoothAnimation implements SmoothAnimation {
    private final SmoothCoastersAPI api;

    public SmoothCoastersSmoothAnimation(SmoothCoastersAPI api) {
        this.api = api;
    }

    @Override
    public boolean isEnabled(Player player) {
        return api.isEnabled(player.getBukkitPlayer());
    }

    @Override
    public void clearRotation(Player player) {
        api.resetRotation(null, player.getBukkitPlayer());
    }

    @Override
    public void setRotation(Player player, Quaternion orientation) {
        float x = (float) orientation.getX();
        float y = (float) orientation.getY();
        float z = (float) orientation.getZ();
        float w = (float) orientation.getW();
        api.setRotation(null, player.getBukkitPlayer(), x, y, z, w, (byte)3);
    }
}
