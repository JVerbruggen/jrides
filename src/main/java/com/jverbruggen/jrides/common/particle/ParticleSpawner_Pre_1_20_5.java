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

package com.jverbruggen.jrides.common.particle;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;

public class ParticleSpawner_Pre_1_20_5 implements ParticleSpawner{

    protected org.bukkit.Particle getBukkitParticle(Particle particle){
        String particleName = switch (particle){
            case TRACK_PARTICLE -> "VILLAGER_HAPPY";
            case SECTION_DIVIDER_PARTICLE -> "CRIT_MAGIC";
            case TRAIN_HEAD_PARTICLE -> "DRIP_WATER";
            case TRAIN_TAIL_PARTICLE -> "DRIP_LAVA";
            case CART_PARTICLE -> "VILLAGER_HAPPY";
            case CART_WHEEL_DISTANCE_PARTICLE -> "HEART";
        };
        return org.bukkit.Particle.valueOf(particleName);
    }

    @Override
    public void spawnParticle(Player player, Particle particle, Vector3 location, int amount, double v0, double v1, double v2) {
        player.getBukkitPlayer().spawnParticle(
                getBukkitParticle(particle),
                location.toBukkitLocation(JRidesPlugin.getWorld()),
                amount, v0, v1, v2, 0);
    }
}
